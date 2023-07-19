package com.arnyminerz.database.entity

import com.arnyminerz.database.dsl.EventPricesTable
import com.arnyminerz.filamagenta.commons.data.Category
import com.arnyminerz.filamagenta.commons.data.types.EventPriceType
import java.time.ZonedDateTime
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.json.JSONObject

class EventPrice(id: EntityID<Long>) : DataEntity<EventPriceType>(id) {
    companion object : LongEntityClass<EventPrice>(EventPricesTable)

    private var _timestamp by EventPricesTable.timestamp
    var price by EventPricesTable.price
    private var _category by EventPricesTable.category

    var event by Event referencedOn EventPricesTable.event

    var timestamp: ZonedDateTime
        get() = ZonedDateTime.parse(_timestamp)
        set(value) { _timestamp = value.toString() }

    var category: Category
        get() = Category.valueOf(_category)
        set(value) { _category = value.name }

    override fun fill(type: EventPriceType) {
        timestamp = type.timestamp
        price = type.price
        category = type.category
        event = Event.findById(type.eventId)!!
    }

    override fun toJSON(): JSONObject {
        TODO("Not yet implemented")
    }
}
