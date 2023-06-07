package com.arnyminerz.database.dsl

import org.jetbrains.exposed.dao.id.IntIdTable

object InventoryItems: IntIdTable() {
    val name = varchar("name", 64)
    var unitPrice = float("unit_price")
}
