package com.example

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.data.AppDatabase
import com.example.data.CurrencyFormatter
import com.example.data.TransactionCategory
import com.example.data.TransactionRepository
import com.example.data.TransactionType
import com.example.ui.LedgerViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  private lateinit var database: AppDatabase
  private lateinit var repository: TransactionRepository
  private val testDispatcher = UnconfinedTestDispatcher()

  @Before
  fun setup() {
    Dispatchers.setMain(testDispatcher)
    val context = ApplicationProvider.getApplicationContext<Context>()
    database =
      Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
        .allowMainThreadQueries()
        .build()
    repository = TransactionRepository(database.transactionDao())
  }

  @After
  fun tearDown() {
    database.close()
    Dispatchers.resetMain()
  }

  @Test
  fun `app name matches Hisab`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("Hisab", appName)
  }

  @Test
  fun `currency formatter formats BDT taka cleanly`() {
    assertEquals("৳0.00", CurrencyFormatter.formatAmount(0.0))
    assertEquals("৳1,250.50", CurrencyFormatter.formatAmount(1250.5))
    assertEquals("-৳300.00", CurrencyFormatter.formatAmount(-300.0))
    assertEquals("+৳5,000.00", CurrencyFormatter.formatSignedAmount(5000.0, TransactionType.INCOME))
    assertEquals("-৳420.75", CurrencyFormatter.formatSignedAmount(420.75, TransactionType.EXPENSE))
  }

  @Test
  fun `adding filtering and deleting transactions updates totals and persists`() = runTest {
    val viewModel = LedgerViewModel(repository)
    val collectJob = launch(testDispatcher) { viewModel.uiState.collect {} }

    // Add Income: Salary 10000
    viewModel.onTypeChange(TransactionType.INCOME)
    viewModel.onCategoryChange(TransactionCategory.SALARY)
    viewModel.onTitleChange("Monthly Salary")
    viewModel.onAmountChange("10000")
    viewModel.submitTransaction()

    // Add Expense: Groceries 1250.50
    viewModel.onTypeChange(TransactionType.EXPENSE)
    viewModel.onCategoryChange(TransactionCategory.FOOD)
    viewModel.onTitleChange("Groceries")
    viewModel.onAmountChange("1250.50")
    viewModel.submitTransaction()

    val stateAfterAdds = viewModel.uiState.first { it.allTransactions.size == 2 }
    assertEquals(10000.0, stateAfterAdds.totalIncome, 0.001)
    assertEquals(1250.50, stateAfterAdds.totalExpenses, 0.001)
    assertEquals(8749.50, stateAfterAdds.totalBalance, 0.001)

    // Filter by Food & Dining
    viewModel.onCategoryFilterChange(TransactionCategory.FOOD.displayName)
    val filteredState = viewModel.uiState.first { it.filteredTransactions.size == 1 }
    assertEquals("Groceries", filteredState.filteredTransactions.first().title)

    // Reset filter to All
    viewModel.onCategoryFilterChange(TransactionCategory.ALL_FILTER)
    val allState = viewModel.uiState.first { it.filteredTransactions.size == 2 }

    // Delete Groceries
    val groceries = allState.allTransactions.first { it.title == "Groceries" }
    viewModel.deleteTransaction(groceries)

    val stateAfterDelete = viewModel.uiState.first { it.allTransactions.size == 1 }
    assertEquals(10000.0, stateAfterDelete.totalIncome, 0.001)
    assertEquals(0.0, stateAfterDelete.totalExpenses, 0.001)
    assertEquals(10000.0, stateAfterDelete.totalBalance, 0.001)
    assertTrue(stateAfterDelete.allTransactions.none { it.title == "Groceries" })

    collectJob.cancel()
  }
}
