package com.arnyminerz.security

import com.arnyminerz.DatabaseTestProto
import com.arnyminerz.database.DatabaseSamples.sampleEvent
import com.arnyminerz.database.DatabaseSamples.sampleUser
import com.arnyminerz.database.entity.Event
import com.arnyminerz.database.entity.EventPayment
import com.arnyminerz.database.entity.User
import com.arnyminerz.filamagenta.commons.data.security.Encryption
import com.arnyminerz.filamagenta.commons.data.security.RSAKeyPairGenerator
import com.arnyminerz.filamagenta.commons.data.types.EventPaymentType
import com.arnyminerz.filamagenta.commons.utils.bytes
import com.arnyminerz.filamagenta.commons.utils.toUUID
import java.time.ZonedDateTime
import java.util.UUID
import javax.crypto.BadPaddingException
import kotlin.test.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class TestEventPaymentVerification : DatabaseTestProto() {
    private lateinit var event: Event
    private lateinit var user: User
    private lateinit var payment: EventPayment

    @Before
    fun `prepare data`() {
        // Requires an event to make the payment into, and a user that will be making the payment.
        runBlocking {
            // Create the event
            event = eventsInterface.new(sampleEvent)

            // Create the user
            user = usersInterface.new(sampleUser, "password" to "password")

            // Generate a UUID and signature for the payment
            val uuid = UUID.randomUUID()
            val uuidBytes = uuid.bytes
            val publicKey = event.decodePublicKey()
            val signature = Encryption.encrypt(publicKey, uuidBytes)

            // Insert the payment
            val paymentData = EventPaymentType(
                0L,
                timestamp = ZonedDateTime.now(),
                uuid = uuid,
                amount = 10.0,
                signature = signature,
                externalReference = null,
                eventId = event.id.value,
                userId = user.id.value
            )
            payment = eventsInterface.newPayment(paymentData)
        }
    }

    @Test
    fun `check event signature verification`() {
        runBlocking {
            val privateKey = event.decodePrivateKey()

            // Now we can verify the signature
            val paymentSignature = payment.signature
            val decryptedSignature = Encryption.decrypt(privateKey, paymentSignature)
            val decryptedUuid = decryptedSignature.toUUID()
            assertEquals(payment.uuid, decryptedUuid, "The signature is not valid.")
        }
    }

    @Test(expected = BadPaddingException::class)
    fun `check event signature verification - fails with incorrect key`() {
        runBlocking {
            val randomKeyPair = RSAKeyPairGenerator.newKey()
            val privateKey = randomKeyPair.private

            // Now we can verify the signature
            val paymentSignature = payment.signature
            val decryptedSignature = Encryption.decrypt(privateKey, paymentSignature)
            val decryptedUuid = decryptedSignature.toUUID()
            assertEquals(payment.uuid, decryptedUuid)
        }
    }
}
