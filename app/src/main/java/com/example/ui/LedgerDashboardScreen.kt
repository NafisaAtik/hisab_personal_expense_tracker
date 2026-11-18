package com.example.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountBalance
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.R
import com.example.ui.components.AddTransactionCard
import com.example.ui.components.BalanceSummarySection
import com.example.ui.components.RecentTransactionsCard
import com.example.ui.theme.LedgerTheme

@Composable
fun LedgerDashboardScreen(viewModel: LedgerViewModel) {
  val state by viewModel.uiState.collectAsStateWithLifecycle()
  val snackbarHostState = remember { SnackbarHostState() }
  val colors = LedgerTheme.colors

  LaunchedEffect(viewModel) {
    viewModel.events.collect { event ->
      when (event) {
        is LedgerEvent.TransactionDeleted -> {
          val result =
            snackbarHostState.showSnackbar(
              message = "Removed \"${event.transaction.title}\"",
              actionLabel = "Undo",
              duration = SnackbarDuration.Short,
            )
          if (result == SnackbarResult.ActionPerformed) {
            viewModel.undoLastDelete()
          }
        }
        LedgerEvent.TransactionAdded -> Unit
      }
    }
  }

  Scaffold(
    containerColor = colors.canvasBackground,
    contentWindowInsets = WindowInsets.safeDrawing,
    snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
  ) { innerPadding ->
    BoxWithConstraints(
      modifier = Modifier.fillMaxSize().padding(innerPadding).imePadding(),
      contentAlignment = Alignment.TopCenter,
    ) {
      val isWideLayout = maxWidth >= 840.dp
      val horizontalPadding = if (isWideLayout) 24.dp else 16.dp

      if (isWideLayout) {
        LazyColumn(
          modifier = Modifier.widthIn(max = 1280.dp).fillMaxSize(),
          contentPadding = PaddingValues(horizontal = horizontalPadding, vertical = 24.dp),
          verticalArrangement = Arrangement.spacedBy(24.dp),
        ) {
          item { LedgerHeader() }
          item {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(24.dp),
              verticalAlignment = Alignment.Top,
            ) {
              Column(
                modifier = Modifier.weight(0.46f),
                verticalArrangement = Arrangement.spacedBy(20.dp),
              ) {
                BalanceSummarySection(
                  totalBalance = state.totalBalance,
                  totalIncome = state.totalIncome,
                  totalExpenses = state.totalExpenses,
                  transactionCount = state.allTransactions.size,
                )
                AddTransactionCard(
                  formState = state.form,
                  onTitleChange = viewModel::onTitleChange,
                  onAmountChange = viewModel::onAmountChange,
                  onTypeChange = viewModel::onTypeChange,
                  onCategoryChange = viewModel::onCategoryChange,
                  onSubmit = viewModel::submitTransaction,
                )
              }

              RecentTransactionsCard(
                transactions = state.filteredTransactions,
                totalTransactionCount = state.allTransactions.size,
                selectedFilter = state.selectedCategoryFilter,
                filterOptions = state.filterOptions,
                onFilterChange = viewModel::onCategoryFilterChange,
                onDeleteTransaction = viewModel::deleteTransaction,
                modifier = Modifier.weight(0.54f),
              )
            }
          }
        }
      } else {
        LazyColumn(
          modifier = Modifier.widthIn(max = 600.dp).fillMaxSize(),
          contentPadding = PaddingValues(horizontal = horizontalPadding, vertical = 16.dp),
          verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
          item { LedgerHeader() }
          item {
            BalanceSummarySection(
              totalBalance = state.totalBalance,
              totalIncome = state.totalIncome,
              totalExpenses = state.totalExpenses,
              transactionCount = state.allTransactions.size,
            )
          }
          item {
            AddTransactionCard(
              formState = state.form,
              onTitleChange = viewModel::onTitleChange,
              onAmountChange = viewModel::onAmountChange,
              onTypeChange = viewModel::onTypeChange,
              onCategoryChange = viewModel::onCategoryChange,
              onSubmit = viewModel::submitTransaction,
            )
          }
          item {
            Spacer(modifier = Modifier.height(4.dp))
            RecentTransactionsCard(
              transactions = state.filteredTransactions,
              totalTransactionCount = state.allTransactions.size,
              selectedFilter = state.selectedCategoryFilter,
              filterOptions = state.filterOptions,
              onFilterChange = viewModel::onCategoryFilterChange,
              onDeleteTransaction = viewModel::deleteTransaction,
            )
          }
        }
      }
    }
  }
}

@Composable
private fun LedgerHeader() {
  val colors = LedgerTheme.colors
  Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically,
  ) {
    Row(
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
      Box(
        modifier =
          Modifier.size(42.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(colors.brandGreen),
        contentAlignment = Alignment.Center,
      ) {
        Icon(
          imageVector = Icons.Rounded.AccountBalance,
          contentDescription = null,
          tint = colors.heroTextPrimary,
          modifier = Modifier.size(21.dp),
        )
      }
      Column {
        Text(
          text = stringResource(R.string.app_name),
          style = MaterialTheme.typography.headlineMedium,
          color = colors.textPrimary,
        )
        Text(
          text = "Personal Expense & Cash-Flow Tracker",
          style = MaterialTheme.typography.bodySmall,
          color = colors.textSecondary,
        )
      }
    }

    Surface(
      shape = RoundedCornerShape(999.dp),
      color = colors.incomeFill,
      border = BorderStroke(1.dp, colors.incomeBorder),
    ) {
      Row(
        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
      ) {
        Box(
          modifier = Modifier.size(7.dp).clip(CircleShape).background(colors.incomeVivid)
        )
        Text(
          text = "BDT (৳)",
          style = MaterialTheme.typography.labelSmall,
          color = colors.incomeText,
        )
      }
    }
  }
}
