package com.arnyminerz.database.types

import com.arnyminerz.database.entity.InventoryItem
import com.arnyminerz.database.entity.User
import java.time.ZonedDateTime
import org.json.JSONObject

data class TransactionType(
    val timestamp: ZonedDateTime,
    val date: ZonedDateTime,
    val amount: Int,
    val pricePerUnit: Float,
    val description: String,
    val user: User,
    val item: InventoryItem?
) : DataType {
    override fun toJSON(): JSONObject = JSONObject().apply {
        put("timestamp", timestamp.toString())
        put("date", date.toString())
        put("amount", amount)
        put("price_per_unit", pricePerUnit)
        put("description", description)
        put("user_id", user.id.value)
        put("item_id", item?.id?.value)
    }
}
