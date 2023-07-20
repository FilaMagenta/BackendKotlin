package com.arnyminerz.database.entity

import com.arnyminerz.database.dsl.InventoryItems
import com.arnyminerz.filamagenta.commons.data.types.InventoryItemType
import com.arnyminerz.filamagenta.commons.utils.jsonOf
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.json.JSONObject

class InventoryItem(id: EntityID<Long>) : DataEntity<InventoryItemType>(id) {
    companion object : LongEntityClass<InventoryItem>(InventoryItems)

    var timestamp by InventoryItems.timestamp
    var name by InventoryItems.name
    var unitPrice by InventoryItems.unitPrice

    override fun fill(type: InventoryItemType) {
        this.timestamp = type.timestamp
        this.name = type.name
        this.unitPrice = type.unitPrice
    }

    override fun toJSON(): JSONObject = jsonOf(
        "timestamp" to timestamp,
        "name" to name,
        "unit_price" to unitPrice
    )
}
