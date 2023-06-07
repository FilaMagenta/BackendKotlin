package com.arnyminerz.database.`interface`

import com.arnyminerz.database.ServerDatabase
import com.arnyminerz.database.entity.InventoryItem
import com.arnyminerz.database.entity.Transaction
import com.arnyminerz.database.types.InventoryItemType
import com.arnyminerz.database.types.TransactionType

class InventoryInterface(
    database: ServerDatabase
): DataObjectInterface<InventoryItemType, InventoryItem, InventoryItem.Companion>(
    database,
    InventoryItem.Companion
) {
    override fun InventoryItem.processExtras(extras: Map<String, String>) { }
}
