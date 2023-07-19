package com.arnyminerz.database.dsl

import com.arnyminerz.database.dsl.DSLConst.DATE_LENGTH
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.Column

private const val TRANSACTION_DESCRIPTION_LENGTH = 256

object Transactions : LongIdTable() {
    val timestamp: Column<String> = varchar("timestamp", DATE_LENGTH)
    val date: Column<String> = varchar("date", DATE_LENGTH)
    val pricePerUnit: Column<Double> = double("unit_price")
    val amount: Column<Long> = long("amount")
    val description: Column<String> = varchar("description", TRANSACTION_DESCRIPTION_LENGTH)

    val user = reference("user", Users)
    val item = reference("item", InventoryItems).nullable()
}
