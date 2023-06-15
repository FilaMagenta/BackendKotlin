package com.arnyminerz.database.types

import org.json.JSONObject

data class InventoryItemType(
    val name: String,
    val unitPrice: Float
) : DataType {
    override fun toJSON(): JSONObject = JSONObject().apply {
        put("name", name)
        put("unit_price", unitPrice)
    }
}
