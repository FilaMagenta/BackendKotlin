package com.arnyminerz.database.connector

import com.arnyminerz.database.ServerDatabase
import com.arnyminerz.database.entity.InventoryItem
import com.arnyminerz.database.types.InventoryItemType

class InventoryInterface(
    database: ServerDatabase
) : DataObjectInterface<InventoryItemType, InventoryItem, InventoryItem.Companion>(
    database,
    InventoryItem.Companion
)
