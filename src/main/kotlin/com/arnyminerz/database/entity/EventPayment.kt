package com.arnyminerz.database.entity

import com.arnyminerz.database.dsl.EventPaymentsTable
import com.arnyminerz.filamagenta.commons.data.types.EventPaymentType
import com.arnyminerz.filamagenta.commons.utils.jsonOf
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.json.JSONObject

class EventPayment(id: EntityID<Long>): DataEntity<EventPaymentType>(id) {
    companion object : LongEntityClass<EventPayment>(EventPaymentsTable)

    private var timestamp by EventPaymentsTable.timestamp
    var uuid by EventPaymentsTable.uuid
    var amount by EventPaymentsTable.amount
    var signature by EventPaymentsTable.signature
    var externalReference by EventPaymentsTable.externalReference

    var event by Event referencedOn EventPaymentsTable.event
    var user by User referencedOn EventPaymentsTable.user

    override fun fill(type: EventPaymentType) {
        timestamp = type.timestamp.toInstant()
        uuid = type.uuid
        amount = type.amount
        signature = type.signature
        externalReference = type.externalReference

        event = Event[type.eventId]
        user = User[type.userId]
    }

    override fun toJSON(): JSONObject = jsonOf(
        "id" to id,
        "timestamp" to timestamp,
        "uuid" to uuid,
        "amount" to amount,
        "signature" to signature,
        "external_reference" to externalReference,
        "event_id" to event.id.value,
        "user_id" to user.id.value
    )
}
