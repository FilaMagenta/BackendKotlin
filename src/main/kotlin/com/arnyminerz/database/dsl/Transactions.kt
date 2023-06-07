package com.arnyminerz.database.dsl

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column

object Transactions: IntIdTable() {
    val date: Column<String> = varchar("date", 64)
    val pricePerUnit: Column<Float> = float("unit_price")
    val amount: Column<Int> = integer("amount")
    val description: Column<String> = varchar("description", 256)

    val user = reference("user", Users)
    val item = reference("item", InventoryItems).nullable()
}