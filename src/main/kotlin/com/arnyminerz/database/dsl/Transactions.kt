package com.arnyminerz.database.dsl

import java.time.Instant
import java.time.LocalDateTime
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.javatime.datetime
import org.jetbrains.exposed.sql.javatime.timestamp

private const val TRANSACTION_DESCRIPTION_LENGTH = 256

object Transactions : LongIdTable() {
    val timestamp: Column<Instant> = timestamp("timestamp").default(Instant.now())
    val date: Column<LocalDateTime> = datetime("date")
    val pricePerUnit: Column<Double> = double("unit_price")
    val amount: Column<Long> = long("amount")
    val description: Column<String> = varchar("description", TRANSACTION_DESCRIPTION_LENGTH)

    val user = reference("user", Users)
    val item = reference("item", InventoryItems).nullable()
}
