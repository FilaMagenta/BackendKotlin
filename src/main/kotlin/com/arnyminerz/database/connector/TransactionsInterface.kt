package com.arnyminerz.database.connector

import com.arnyminerz.database.ServerDatabase
import com.arnyminerz.database.dsl.Transactions
import com.arnyminerz.database.entity.Transaction
import com.arnyminerz.database.entity.User
import com.arnyminerz.filamagenta.commons.data.types.TransactionType
import org.jetbrains.exposed.sql.SizedIterable

class TransactionsInterface(
    database: ServerDatabase
) : DataObjectInterface<TransactionType, Transaction, Transaction.Companion>(
    database,
    Transaction.Companion
) {
    suspend fun <Result> findForUser(user: User, block: suspend (transactions: SizedIterable<Transaction>) -> Result) =
        database.transaction {
            val list = Transaction.find { Transactions.user eq user.id }
            block(list)
        }
}
