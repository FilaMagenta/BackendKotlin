package com.arnyminerz.database.entity

import com.arnyminerz.filamagenta.commons.data.types.EventPriceType
import org.jetbrains.exposed.dao.id.EntityID
import org.json.JSONObject

class EventPrice(id: EntityID<Long>) : DataEntity<EventPriceType>(id) {
    override fun fill(type: EventPriceType) {
        TODO("Not yet implemented")
    }

    override fun toJSON(): JSONObject {
        TODO("Not yet implemented")
    }
}
