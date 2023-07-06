package com.arnyminerz.database.entity

import com.arnyminerz.database.dsl.EventTables
import com.arnyminerz.database.dsl.Events
import com.arnyminerz.database.dsl.UserAssistances
import com.arnyminerz.database.types.EventType
import com.arnyminerz.security.RSAKeyPairGenerator
import com.arnyminerz.utils.jsonOf
import com.arnyminerz.utils.toJSON
import com.arnyminerz.utils.toRSAPrivateKey
import com.arnyminerz.utils.toRSAPublicKey
import java.security.PrivateKey
import java.security.PublicKey
import java.time.ZonedDateTime
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.json.JSONObject

class Event(id: EntityID<Int>) : DataEntity<EventType>(id) {
    companion object : IntEntityClass<Event>(Events)

    var timestamp by Events.timestamp
    var name by Events.name
    var description by Events.description
    var date by Events.date
    var until by Events.until
    var reservations by Events.reservations
    var maxGuests by Events.maxGuests

    var publicKey by Events.publicKey
    var privateKey by Events.privateKey

    val assistants by UserAssistance referrersOn UserAssistances.event

    val tables by EventTable referrersOn EventTables.event

    /**
     * Generates a random key pair and assigns it to [publicKey] and [privateKey].
     */
    fun provideRandomKey() {
        val keyPair = RSAKeyPairGenerator.newKey()
        publicKey = keyPair.public.toJSON().toString()
        privateKey = keyPair.private.toJSON().toString()
    }

    /**
     * Decodes the stored [publicKey] into a [PublicKey] to use for encryption.
     */
    fun decodePublicKey(): PublicKey = JSONObject(publicKey).toRSAPublicKey()

    /**
     * Decodes the stored [privateKey] into a [PrivateKey] to use for decryption.
     */
    fun decodePrivateKey(): PrivateKey = JSONObject(privateKey).toRSAPrivateKey()

    /**
     * Gets the event start date.
     */
    fun getStart(): ZonedDateTime = ZonedDateTime.parse(date)

    /**
     * Gets the event end date (might be null).
     */
    fun getEnd(): ZonedDateTime? = until?.let { ZonedDateTime.parse(it) }

    /**
     * Gets the last moment when reservations can be made.
     */
    fun getReservationsEndDate(): ZonedDateTime? = reservations?.let { ZonedDateTime.parse(it) }

    override fun toJSON(): JSONObject = jsonOf(
        "id" to id.value,
        "timestamp" to timestamp,
        "name" to name,
        "description" to description,
        "date" to date,
        "until" to until,
        "reservations" to reservations,
        "max_guests" to maxGuests,
        "public_key" to JSONObject(publicKey)
    )

    override fun fill(type: EventType) {
        name = type.name
        description = type.description
        date = type.date.toString()
        until = type.until?.toString()
        reservations = type.reservations?.toString()
        maxGuests = type.maxGuests
        publicKey = type.keyPair.public.toJSON().toString()
        privateKey = type.keyPair.private.toJSON().toString()
    }
}
