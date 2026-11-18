package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.Transaction
import com.example.data.TransactionCategory
import com.example.data.TransactionRepository
import com.example.data.TransactionType
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class LedgerFormState(
  val title: String = "",
  val amount: String = "",
  val type: TransactionType = TransactionType.EXPENSE,
  val category: TransactionCategory = TransactionCategory.FOOD,
  val titleError: String? = null,
  val amountError: String? = null,
)

data class LedgerUiState(
  val allTransactions: List<Transaction> = emptyList(),
  val filteredTransactions: List<Transaction> = emptyList(),
  val selectedCategoryFilter: String = TransactionCategory.ALL_FILTER,
  val filterOptions: List<String> = listOf(TransactionCategory.ALL_FILTER),
  val totalBalance: Double = 0.0,
  val totalIncome: Double = 0.0,
  val totalExpenses: Double = 0.0,
  val form: LedgerFormState = LedgerFormState(),
)

sealed interface LedgerEvent {
  data class TransactionDeleted(val transaction: Transaction) : LedgerEvent

  data object TransactionAdded : LedgerEvent
}

class LedgerViewModel(private val repository: TransactionRepository) : ViewModel() {

  private val formState = MutableStateFlow(LedgerFormState())
  private val selectedFilter = MutableStateFlow(TransactionCategory.ALL_FILTER)
  private var lastDeletedTransaction: Transaction? = null

  private val _events = MutableSharedFlow<LedgerEvent>()
  val events: SharedFlow<LedgerEvent> = _events.asSharedFlow()

  val uiState: StateFlow<LedgerUiState> =
    combine(repository.allTransactions, selectedFilter, formState) { transactions, filter, form ->
        val totalIncome =
          transactions.filter { it.type == TransactionType.INCOME }.sumOf { it.amount }
        val totalExpenses =
          transactions.filter { it.type == TransactionType.EXPENSE }.sumOf { it.amount }
        val totalBalance = totalIncome - totalExpenses

        val filterOptions = buildList {
          add(TransactionCategory.ALL_FILTER)
          val defaultNames = TransactionCategory.entries.map { it.displayName }
          val presentNames = transactions.map { it.category }.distinct()
          addAll((defaultNames + presentNames).distinct())
        }

        val activeFilter = if (filter in filterOptions) filter else TransactionCategory.ALL_FILTER
        val filtered =
          if (activeFilter == TransactionCategory.ALL_FILTER) {
            transactions
          } else {
            transactions.filter { it.category == activeFilter }
          }

        LedgerUiState(
          allTransactions = transactions,
          filteredTransactions = filtered,
          selectedCategoryFilter = activeFilter,
          filterOptions = filterOptions,
          totalBalance = totalBalance,
          totalIncome = totalIncome,
          totalExpenses = totalExpenses,
          form = form,
        )
      }
      .stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = LedgerUiState(),
      )

  fun onTitleChange(newTitle: String) {
    formState.update { it.copy(title = newTitle, titleError = null) }
  }

  fun onAmountChange(rawInput: String) {
    val sanitized = sanitizeDecimalInput(rawInput)
    if (sanitized != null) {
      formState.update { it.copy(amount = sanitized, amountError = null) }
    }
  }

  fun onTypeChange(newType: TransactionType) {
    formState.update { current ->
      val validCategories = TransactionCategory.categoriesForType(newType)
      val nextCategory =
        if (current.category in validCategories) {
          current.category
        } else {
          validCategories.first()
        }
      current.copy(type = newType, category = nextCategory)
    }
  }

  fun onCategoryChange(category: TransactionCategory) {
    formState.update { it.copy(category = category) }
  }

  fun onCategoryFilterChange(categoryFilter: String) {
    selectedFilter.value = categoryFilter
  }

  fun submitTransaction() {
    val current = formState.value
    val trimmedTitle = current.title.trim()
    val parsedAmount = current.amount.toDoubleOrNull()

    val titleError = if (trimmedTitle.isEmpty()) "Enter a description" else null
    val amountError =
      when {
        current.amount.isBlank() -> "Enter an amount"
        parsedAmount == null || parsedAmount <= 0.0 -> "Amount must be greater than 0"
        else -> null
      }

    if (titleError != null || amountError != null) {
      formState.update { it.copy(titleError = titleError, amountError = amountError) }
      return
    }

    val safeAmount = parsedAmount!!
    viewModelScope.launch {
      repository.addTransaction(
        title = trimmedTitle,
        amount = safeAmount,
        type = current.type,
        category = current.category.displayName,
      )
      formState.update {
        it.copy(title = "", amount = "", titleError = null, amountError = null)
      }
      _events.emit(LedgerEvent.TransactionAdded)
    }
  }

  fun deleteTransaction(transaction: Transaction) {
    lastDeletedTransaction = transaction
    viewModelScope.launch {
      repository.deleteTransaction(transaction.id)
      _events.emit(LedgerEvent.TransactionDeleted(transaction))
    }
  }

  fun undoLastDelete() {
    val deleted = lastDeletedTransaction ?: return
    lastDeletedTransaction = null
    viewModelScope.launch { repository.restoreTransaction(deleted) }
  }

  private fun sanitizeDecimalInput(input: String): String? {
    if (input.isEmpty()) return ""
    var dotSeen = false
    var decimalsCount = 0
    val result = StringBuilder()
    for (ch in input) {
      when {
        ch.isDigit() -> {
          if (dotSeen) {
            if (decimalsCount >= 2) return null
            decimalsCount++
          }
          result.append(ch)
        }
        ch == '.' && !dotSeen -> {
          dotSeen = true
          if (result.isEmpty()) result.append('0')
          result.append(ch)
        }
        else -> return null
      }
    }
    return result.toString()
  }

  class Factory(private val repository: TransactionRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
      if (modelClass.isAssignableFrom(LedgerViewModel::class.java)) {
        return LedgerViewModel(repository) as T
      }
      throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
  }
}
