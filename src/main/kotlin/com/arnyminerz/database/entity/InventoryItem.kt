package com.arnyminerz.database.entity

import com.arnyminerz.database.dsl.InventoryItems
import com.arnyminerz.filamagenta.commons.data.types.InventoryItemType
import com.arnyminerz.filamagenta.commons.utils.jsonOf
import java.time.ZonedDateTime
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.json.JSONObject

class InventoryItem(id: EntityID<Long>) : DataEntity<InventoryItemType>(id) {
    companion object : LongEntityClass<InventoryItem>(InventoryItems)

    private var _timestamp by InventoryItems.timestamp
    var name by InventoryItems.name
    var unitPrice by InventoryItems.unitPrice

    var timestamp: ZonedDateTime
        get() = ZonedDateTime.parse(_timestamp)
        set(value) { _timestamp = value.toString() }

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
