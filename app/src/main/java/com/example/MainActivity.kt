package com.example

import android.graphics.Color as AndroidColor
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.AppDatabase
import com.example.data.TransactionRepository
import com.example.ui.LedgerDashboardScreen
import com.example.ui.LedgerViewModel
import com.example.ui.theme.LedgerMinimalTheme

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge(
      statusBarStyle = SystemBarStyle.light(AndroidColor.TRANSPARENT, AndroidColor.TRANSPARENT),
      navigationBarStyle = SystemBarStyle.light(AndroidColor.TRANSPARENT, AndroidColor.TRANSPARENT),
    )

    val database = AppDatabase.getInstance(applicationContext)
    val repository = TransactionRepository(database.transactionDao())

    setContent {
      LedgerMinimalTheme {
        val ledgerViewModel: LedgerViewModel =
          viewModel(factory = LedgerViewModel.Factory(repository))
        LedgerDashboardScreen(viewModel = ledgerViewModel)
      }
    }
  }
}
