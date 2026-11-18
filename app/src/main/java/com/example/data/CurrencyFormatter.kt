package com.example.data

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale
import kotlin.math.abs

object CurrencyFormatter {
  private const val SYMBOL = "৳"

  private fun createFormatter(): DecimalFormat {
    val symbols = DecimalFormatSymbols(Locale.US)
    return DecimalFormat("#,##0.00", symbols)
  }

  fun formatAmount(amount: Double): String {
    val formatted = createFormatter().format(abs(amount))
    return if (amount < -0.004) "-$SYMBOL$formatted" else "$SYMBOL$formatted"
  }

  fun formatSignedAmount(amount: Double, type: TransactionType): String {
    val formatted = createFormatter().format(abs(amount))
    val sign = if (type == TransactionType.INCOME) "+" else "-"
    return "$sign$SYMBOL$formatted"
  }
}
