package com.arnyminerz.database.`interface`

import com.arnyminerz.database.ServerDatabase
import com.arnyminerz.database.dsl.Transactions
import com.arnyminerz.database.dsl.Users
import com.arnyminerz.database.entity.Transaction
import com.arnyminerz.database.entity.User
import com.arnyminerz.database.types.TransactionType
import org.jetbrains.exposed.sql.SizedIterable
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class TransactionsInterface(
    database: ServerDatabase
): DataObjectInterface<TransactionType, Transaction, Transaction.Companion>(
    database,
    Transaction.Companion
) {
    override fun Transaction.processExtras(extras: Map<String, String>) { }

    suspend fun <Result> findForUser(user: User, block: suspend (transactions: SizedIterable<Transaction>) -> Result) = database.transaction {
        val list = Transaction.find { Transactions.user eq user.id }
        block(list)
    }
}
