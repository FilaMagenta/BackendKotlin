package com.arnyminerz.database.entity

import com.arnyminerz.database.dsl.Transactions
import com.arnyminerz.database.types.TransactionType
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.json.JSONObject

class Transaction(id: EntityID<Int>): DataEntity<TransactionType>(id) {
    companion object: IntEntityClass<Transaction>(Transactions)

    var date by Transactions.date
    var amount by Transactions.amount
    var pricePerUnit by Transactions.pricePerUnit
    var description by Transactions.description

    var user by User referencedOn Transactions.user
    var item by InventoryItem optionalReferencedOn Transactions.item

    val total: Float
        get() = amount * pricePerUnit

    override fun fill(type: TransactionType) {
        this.date = type.date.toString()
        this.amount = type.amount
        this.pricePerUnit = type.pricePerUnit
        this.description = type.description

        this.user = type.user
        this.item = type.item
    }

    override fun toJSON(): JSONObject = JSONObject().apply {
        put("date", date)
        put("amount", amount)
        put("price_per_unit", pricePerUnit)
        put("description", description)
        put("user_id", user.id.value)
        put("item_id", item?.id?.value)
    }
}
