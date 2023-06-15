package com.arnyminerz.database.entity

import com.arnyminerz.database.dsl.InventoryItems
import com.arnyminerz.database.types.InventoryItemType
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.json.JSONObject

class InventoryItem(id: EntityID<Int>) : DataEntity<InventoryItemType>(id) {
    companion object : IntEntityClass<InventoryItem>(InventoryItems)

    var name by InventoryItems.name
    var unitPrice by InventoryItems.unitPrice

    override fun fill(type: InventoryItemType) {
        this.name = type.name
        this.unitPrice = type.unitPrice
    }

    override fun toJSON(): JSONObject = JSONObject().apply {
        put("name", name)
        put("unit_price", unitPrice)
    }
}
