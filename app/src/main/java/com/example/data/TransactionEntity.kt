package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transactions")
data class TransactionEntity(
  @PrimaryKey(autoGenerate = true) val id: Long = 0L,
  val title: String,
  val amount: Double,
  val type: String,
  val category: String,
  val createdAtMillis: Long = System.currentTimeMillis(),
) {
  fun toDomain(): Transaction =
    Transaction(
      id = id,
      title = title,
      amount = amount,
      type = runCatching { TransactionType.valueOf(type) }.getOrDefault(TransactionType.EXPENSE),
      category = category,
      createdAtMillis = createdAtMillis,
    )

  companion object {
    fun fromDomain(transaction: Transaction): TransactionEntity =
      TransactionEntity(
        id = transaction.id,
        title = transaction.title,
        amount = transaction.amount,
        type = transaction.type.name,
        category = transaction.category,
        createdAtMillis = transaction.createdAtMillis,
      )
  }
}
