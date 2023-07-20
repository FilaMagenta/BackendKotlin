package com.arnyminerz.database.entity

import com.arnyminerz.database.dsl.EventPricesTable
import com.arnyminerz.database.dsl.EventTables
import com.arnyminerz.database.dsl.Events
import com.arnyminerz.database.dsl.UserAssistances
import com.arnyminerz.filamagenta.commons.data.Category
import com.arnyminerz.filamagenta.commons.data.security.RSAKeyPairGenerator
import com.arnyminerz.filamagenta.commons.data.types.EventType
import com.arnyminerz.filamagenta.commons.utils.jsonOf
import com.arnyminerz.filamagenta.commons.utils.toJSON
import com.arnyminerz.filamagenta.commons.utils.toRSAPrivateKey
import com.arnyminerz.filamagenta.commons.utils.toRSAPublicKey
import java.security.PrivateKey
import java.security.PublicKey
import java.time.ZoneId
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.json.JSONObject

class Event(id: EntityID<Long>) : DataEntity<EventType>(id) {
    companion object : LongEntityClass<Event>(Events)

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

    private val _prices by EventPrice referrersOn EventPricesTable.event

    /**
     * A property representing the prices of different categories.
     *
     * This property returns a map where the keys are category objects and the values are the corresponding prices.
     *
     * **Make sure to be in a transaction when calling this method**.
     *
     * @return The map of category prices.
     */
    val prices: Map<Category, Double>
        get() = _prices.associate { it.category to it.price }

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

    override fun toJSON(): JSONObject = jsonOf(
        "id" to id.value,
        "timestamp" to timestamp,
        "name" to name,
        "description" to description,
        "date" to date,
        "until" to until,
        "reservations" to reservations,
        "max_guests" to maxGuests,
        "key_pair" to jsonOf(
            "public" to JSONObject(publicKey),
            "private" to JSONObject(privateKey),
        )
    )

    override fun fill(type: EventType) {
        name = type.name
        description = type.description
        date = type.date.atZone(ZoneId.systemDefault()).toLocalDateTime()
        until = type.until?.atZone(ZoneId.systemDefault())?.toLocalDateTime()
        reservations = type.reservations?.atZone(ZoneId.systemDefault())?.toLocalDateTime()
        maxGuests = type.maxGuests
        publicKey = type.publicKey.toJSON().toString()
        privateKey = type.privateKey!!.toJSON().toString()
    }
}
