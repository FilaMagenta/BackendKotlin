package com.arnyminerz.application.profile

import com.arnyminerz.application.ApplicationTestProto
import com.arnyminerz.database.dsl.CategoryInformations
import com.arnyminerz.filamagenta.commons.data.Category
import com.arnyminerz.filamagenta.commons.errors.Errors
import com.arnyminerz.filamagenta.commons.utils.getJSONObjectOrNull
import com.arnyminerz.filamagenta.commons.utils.getStringOrNull
import com.arnyminerz.filamagenta.commons.utils.jsonOf
import com.arnyminerz.utils.assertEqualsJson
import com.arnyminerz.utils.assertFailure
import com.arnyminerz.utils.assertSuccess
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import org.junit.Test

class ApplicationTestUpdateCategoryData : ApplicationTestProto() {
    @Test
    fun `test that regular users can't update category data`() = testLoggedIn { token ->
        client.post("/v1/profile/category/abc") {
            header("Authorization", "Bearer $token")
        }.apply {
            assertFailure(Errors.MissingPermission)
        }
    }

    @Test
    fun `try modifying data of UNKNOWN`() = testLoggedInAdmin { token ->
        client.post("/v1/profile/category/${Category.UNKNOWN}") {
            header("Authorization", "Bearer $token")
        }.apply {
            assertFailure(Errors.CategoryIllegal)
        }
    }

    @Test
    fun `try modifying data of an invalid category`() = testLoggedInAdmin { token ->
        client.post("/v1/profile/category/abc") {
            header("Authorization", "Bearer $token")
        }.apply {
            assertFailure(Errors.CategoryInvalid)
        }
    }

    private val sampleCategoryDataJson = jsonOf(
        "price" to 600,
        "age_min" to 0,
        "age_max" to 16,
        "votes" to false,
        "diana" to true,
        "diana_2" to true,
        "esquadra" to false,
        "entrada" to true,
        "processo" to false,
        "alardo" to true,
        "pays_dina" to CategoryInformations.PAYS_NO,
        "pays_mig_any_and_musics" to CategoryInformations.PAYS_NO,
        "pays_assaig" to CategoryInformations.PAYS_FULL,
        "pays_entradeta" to CategoryInformations.PAYS_INCLUDED,
        "pays_esmorzar_gloria" to CategoryInformations.PAYS_NO,
        "pays_esmorzar_festes" to CategoryInformations.PAYS_INCLUDED,
        "pays_dinar_entrada" to CategoryInformations.PAYS_NO,
        "pays_dinar_sant_jordi" to CategoryInformations.PAYS_FULL,
        "pays_sopar_sant_jordi" to CategoryInformations.PAYS_NO,
        "pays_dinar_trons" to CategoryInformations.PAYS_FULL
    )

    @Test
    fun `test updating category data`() = testLoggedInAdmin { token ->
        val userId = usersInterface.findWithNif(registerSampleData.getValue("nif")) { it }!!.id

        // Set the user's category
        client.post("/v1/profile/$userId/category") {
            header("Authorization", "Bearer $token")
            setBody(
                jsonOf("category" to Category.FESTER.name).toString()
            )
        }.assertSuccess()

        // Check that at first there's no category information
        client.get("/v1/profile") {
            header("Authorization", "Bearer $token")
        }.apply {
            assertSuccess {
                assertNotNull(it)
                assertEquals(Category.FESTER.name, it.getStringOrNull("category"))
                assertNull(it.getJSONObjectOrNull("category_information"))
            }
        }

        // Update the category data
        client.post("/v1/profile/category/${Category.FESTER}") {
            header("Authorization", "Bearer $token")
            setBody(
                sampleCategoryDataJson.toString()
            )
        }.apply {
            assertSuccess()
        }

        // Now check that the data has been successfully set
        client.get("/v1/profile") {
            header("Authorization", "Bearer $token")
        }.apply {
            assertSuccess {
                assertNotNull(it)
                assertEquals(Category.FESTER.name, it.getStringOrNull("category"))
                val categoryInformation = it.getJSONObjectOrNull("category_information")
                assertNotNull(categoryInformation)
                assertEqualsJson(sampleCategoryDataJson, categoryInformation)
            }
        }
    }
}
