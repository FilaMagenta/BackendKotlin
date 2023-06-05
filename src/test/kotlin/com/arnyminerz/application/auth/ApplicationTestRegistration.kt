package com.arnyminerz.application.auth

import com.arnyminerz.application.ApplicationTestProto
import com.arnyminerz.database.ServerDatabase
import com.arnyminerz.errors.Errors
import com.arnyminerz.utils.assertFailure
import com.arnyminerz.utils.assertSuccess
import com.arnyminerz.utils.change
import com.arnyminerz.utils.extract
import io.ktor.http.HttpStatusCode
import kotlin.test.assertEquals
import org.junit.Test

class ApplicationTestRegistration: ApplicationTestProto() {
    @Test
    fun test_register_missingNif() = test {
        register(registerSampleData.extract("nif")) { assertFailure(Errors.MissingNifBody) }
    }

    @Test
    fun test_register_invalidNif() = test {
        register(registerSampleData.change("nif", "123")) { assertFailure(Errors.NifInvalid) }
    }

    @Test
    fun test_register_missingName() = test {
        register(registerSampleData.extract("name")) { assertFailure(Errors.MissingNameBody) }
    }

    @Test
    fun test_register_missingSurname() = test {
        register(registerSampleData.extract("surname")) { assertFailure(Errors.MissingSurnameBody) }
    }

    @Test
    fun test_register_missingEmail() = test {
        register(registerSampleData.extract("email")) { assertFailure(Errors.MissingEmailBody) }
    }

    @Test
    fun test_register_invalidEmail() = test {
        register(registerSampleData.change("email", "invalid")) { assertFailure(Errors.EmailInvalid) }
    }

    @Test
    fun test_register_missingPassword() = test {
        register(registerSampleData.extract("password")) { assertFailure(Errors.MissingPasswordBody) }
    }

    @Test
    fun test_register_correct() = test {
        register(registerSampleData) { assertSuccess(HttpStatusCode.Created) }

        // Make sure the user is now registered
        ServerDatabase.instance.usersInterface.getAll { users ->
            assertEquals(1, users.count())

            val user = users.first()
            assertEquals(registerSampleData["nif"], user.nif)
            assertEquals(registerSampleData["name"], user.name)
            assertEquals(registerSampleData["surname"], user.surname)
            assertEquals(registerSampleData["email"], user.email)
        }
    }
}