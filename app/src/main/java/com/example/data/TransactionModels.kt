package com.example.data

enum class TransactionType {
  INCOME,
  EXPENSE,
}

enum class TransactionCategory(
  val displayName: String,
  val defaultType: TransactionType?,
) {
  FOOD("Food & Dining", TransactionType.EXPENSE),
  TRANSPORT("Transport", TransactionType.EXPENSE),
  HOUSING("Housing & Rent", TransactionType.EXPENSE),
  UTILITIES("Utilities", TransactionType.EXPENSE),
  SHOPPING("Shopping", TransactionType.EXPENSE),
  HEALTH("Health", TransactionType.EXPENSE),
  ENTERTAINMENT("Entertainment", TransactionType.EXPENSE),
  SALARY("Salary", TransactionType.INCOME),
  FREELANCE("Freelance", TransactionType.INCOME),
  BUSINESS("Business", TransactionType.INCOME),
  INVESTMENT("Investment", TransactionType.INCOME),
  GIFT("Gift", TransactionType.INCOME),
  OTHER("Other", null);

  companion object {
    const val ALL_FILTER = "All"

    fun categoriesForType(type: TransactionType): List<TransactionCategory> =
      entries.filter { it.defaultType == type || it.defaultType == null }

    fun fromDisplayName(name: String): TransactionCategory =
      entries.firstOrNull { it.displayName == name } ?: OTHER
  }
}

data class Transaction(
  val id: Long = 0L,
  val title: String,
  val amount: Double,
  val type: TransactionType,
  val category: String,
  val createdAtMillis: Long = System.currentTimeMillis(),
)
