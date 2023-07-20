package com.arnyminerz.database

import com.arnyminerz.filamagenta.commons.data.Category
import com.arnyminerz.filamagenta.commons.data.security.RSAKeyPairGenerator
import com.arnyminerz.filamagenta.commons.data.security.permissions.Role
import com.arnyminerz.filamagenta.commons.data.types.EventType
import com.arnyminerz.filamagenta.commons.data.types.UserType
import java.time.Instant
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime

object DatabaseSamples {
    val sampleEvent = RSAKeyPairGenerator.newKey().let { keyPair ->
        EventType(
            0,
            ZonedDateTime
                .of(2023, 10, 13, 15, 10, 0, 0, ZoneId.of("UTC"))
                .toInstant(),
            "Test Event",
            "Test event Description",
            ZonedDateTime
                .of(2023, 5, 10, 12, 5, 0, 0, ZoneOffset.UTC)
                .toInstant(),
            null,
            null,
            -1,
            keyPair.public,
            keyPair.private
        )
    }

    val sampleUser = UserType(
        0,
        Instant.now(),
        "12345678Z",
        Category.FESTER,
        Role.DEFAULT,
        "Testing",
        "User",
        "example@mail.com"
    )
}
