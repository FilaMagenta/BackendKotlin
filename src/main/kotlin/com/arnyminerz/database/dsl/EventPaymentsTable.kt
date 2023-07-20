package com.arnyminerz.database.dsl

import java.time.Instant
import java.time.ZonedDateTime
import java.util.UUID
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.javatime.timestamp

object EventPaymentsTable : LongIdTable() {
    /**
     * Holds the timestamp of a record in the database. This is the moment at which the purchase was made.
     */
    val timestamp: Column<Instant> = timestamp("timestamp").default(ZonedDateTime.now().toInstant())

    /**
     * Represents a universally unique identifier (UUID) column. Must be unique.
     *
     * Example: `ee037bd2-e084-470c-8afc-64e3053196f6`
     */
    val uuid: Column<UUID> = uuid("uuid")
        .uniqueIndex("uuid")

    /**
     * The monetary amount of the transaction.
     */
    val amount: Column<Double> = double("amount")

    /**
     * Holds a signature that can be used for verifying the transaction. Holds a signature made with the event's
     * private key, and can be verified with the public key.
     *
     * This signature, when decoded, must match [uuid].
     */
    val signature: Column<ByteArray> = binary("signature")

    /**
     * If any, external references of the transaction. For example, bank transfer identifiers.
     */
    val externalReference: Column<String?> = varchar("external_reference", DSLConst.EXTERNAL_REFERENCE_LENGTH)
        .nullable()

    /**
     * The event for which this purchase was made.
     */
    val event = reference("event", Events)

    /**
     * The user that made the purchase.
     */
    val user = reference("user", Users)
}
