package com.example.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TransactionRepository(private val dao: TransactionDao) {
  val allTransactions: Flow<List<Transaction>> =
    dao.observeAllTransactions().map { entities -> entities.map { it.toDomain() } }

  suspend fun addTransaction(
    title: String,
    amount: Double,
    type: TransactionType,
    category: String,
    createdAtMillis: Long = System.currentTimeMillis(),
  ): Long {
    val entity =
      TransactionEntity(
        title = title.trim(),
        amount = amount,
        type = type.name,
        category = category,
        createdAtMillis = createdAtMillis,
      )
    return dao.insertTransaction(entity)
  }

  suspend fun restoreTransaction(transaction: Transaction): Long =
    dao.insertTransaction(TransactionEntity.fromDomain(transaction))

  suspend fun deleteTransaction(id: Long) {
    dao.deleteTransactionById(id)
  }
}
