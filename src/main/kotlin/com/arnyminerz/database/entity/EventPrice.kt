package com.arnyminerz.database.entity

import com.arnyminerz.database.dsl.EventPricesTable
import com.arnyminerz.filamagenta.commons.data.types.EventPriceType
import com.arnyminerz.filamagenta.commons.utils.jsonOf
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.json.JSONObject

class EventPrice(id: EntityID<Long>) : DataEntity<EventPriceType>(id) {
    companion object : LongEntityClass<EventPrice>(EventPricesTable)

    var timestamp by EventPricesTable.timestamp
    var price by EventPricesTable.price
    var category by EventPricesTable.category

    var event by Event referencedOn EventPricesTable.event

    override fun fill(type: EventPriceType) {
        timestamp = type.timestamp
        price = type.price
        category = type.category
        event = Event.findById(type.eventId)!!
    }

    override fun toJSON(): JSONObject = jsonOf(
        "id" to id.value,
        "timestamp" to timestamp,
        "price" to price,
        "category" to category,
        "event_id" to event.id.value
    )
}
