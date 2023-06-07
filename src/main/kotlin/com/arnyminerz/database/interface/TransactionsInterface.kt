package com.arnyminerz.database.`interface`

import com.arnyminerz.database.ServerDatabase
import com.arnyminerz.database.entity.Transaction
import com.arnyminerz.database.types.TransactionType

class TransactionsInterface(
    database: ServerDatabase
): DataObjectInterface<TransactionType, Transaction, Transaction.Companion>(
    database,
    Transaction.Companion
) {
    override fun Transaction.processExtras(extras: Map<String, String>) { }
}
