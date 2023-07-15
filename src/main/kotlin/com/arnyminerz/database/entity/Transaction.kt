package com.arnyminerz.database.entity

import com.arnyminerz.database.dsl.Transactions
import com.arnyminerz.filamagenta.commons.data.types.TransactionType
import com.arnyminerz.filamagenta.commons.utils.jsonOf
import java.time.ZonedDateTime
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.json.JSONObject

class Transaction(id: EntityID<Long>) : DataEntity<TransactionType>(id) {
    companion object : LongEntityClass<Transaction>(Transactions)

    private var _timestamp by Transactions.timestamp
    var date by Transactions.date
    var amount by Transactions.amount
    var pricePerUnit by Transactions.pricePerUnit
    var description by Transactions.description

    var user by User referencedOn Transactions.user
    var item by InventoryItem optionalReferencedOn Transactions.item

    var timestamp: ZonedDateTime
        get() = ZonedDateTime.parse(_timestamp)
        set(value) { _timestamp = value.toString() }

    val total: Double
        get() = amount * pricePerUnit

    /**
     * Fills the instance with the data provided in [type]. **Requires to be in a transaction.**
     */
    override fun fill(type: TransactionType) {
        this.timestamp = type.timestamp
        this.date = type.date.toString()
        this.amount = type.amount
        this.pricePerUnit = type.pricePerUnit
        this.description = type.description

        this.user = User[type.userId]
        this.item = type.itemId?.let { InventoryItem[it] }
    }

    override fun toJSON(): JSONObject = jsonOf(
        "id" to id.value,
        "timestamp" to _timestamp,
        "date" to date,
        "amount" to amount,
        "price_per_unit" to pricePerUnit,
        "description" to description,
        "user_id" to user.id.value,
        "item_id" to item?.id?.value
    )
}
