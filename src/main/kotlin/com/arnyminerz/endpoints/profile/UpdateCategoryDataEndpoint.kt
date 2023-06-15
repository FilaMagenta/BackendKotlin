package com.arnyminerz.endpoints.profile

import com.arnyminerz.data.Category
import com.arnyminerz.database.dsl.CategoryInformations
import com.arnyminerz.database.entity.User
import com.arnyminerz.endpoints.arguments.Arguments
import com.arnyminerz.endpoints.arguments.called
import com.arnyminerz.endpoints.protos.AuthenticatedEndpoint
import com.arnyminerz.errors.Errors
import com.arnyminerz.security.permissions.Permissions
import com.arnyminerz.utils.respondFailure
import com.arnyminerz.utils.respondSuccess
import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.call
import io.ktor.server.util.getValue
import io.ktor.util.pipeline.PipelineContext

object UpdateCategoryDataEndpoint : AuthenticatedEndpoint(Permissions.Users.UpdateCategoryInfo) {
    override suspend fun PipelineContext<*, ApplicationCall>.endpoint(user: User) {
        val categoryName: String by call.parameters
        val category = try {
            Category.valueOf(categoryName)
        } catch (_: IllegalArgumentException) {
            return call.respondFailure(Errors.CategoryInvalid)
        }
        if (category == Category.UNKNOWN) {
            return call.respondFailure(Errors.CategoryIllegal)
        }

        val ageMin by called { Arguments.AgeMin }
        val ageMax by called { Arguments.AgeMax }

        val price by called { Arguments.Price }

        val votesMeeting by called { Arguments.Votes }
        val diana by called { Arguments.Diana }
        val diana2 by called { Arguments.Diana2 }
        val esquadra by called { Arguments.Esquadra }
        val entrada by called { Arguments.Entrada }
        val processo by called { Arguments.Processo }
        val alardo by called { Arguments.Alardo }
        val rightsData =
            CategoryInformations.RightsData(votesMeeting, diana, diana2, esquadra, entrada, processo, alardo)

        val paysDina by called { Arguments.PaysDina }
        val paysMigAnyAndMusics by called { Arguments.PaysMigAnyAndMusics }
        val paysAssaig by called { Arguments.PaysAssaig }
        val paysEntradetaOficial by called { Arguments.PaysEntradeta }
        val paysEsmorzarGloria by called { Arguments.PaysEsmorzarGloria }
        val paysEsmorzarFestes by called { Arguments.PaysEsmorzarFestes }
        val paysDinarEntrada by called { Arguments.PaysDinarEntrada }
        val paysDinarSantJordi by called { Arguments.PaysDinarSantJordi }
        val paysSoparSantJordi by called { Arguments.PaysSoparSantJordi }
        val paysDinarTrons by called { Arguments.PaysDinarTrons }
        val paysData = CategoryInformations.PaysData(
            paysDina,
            paysMigAnyAndMusics,
            paysAssaig,
            paysEntradetaOficial,
            paysEsmorzarGloria,
            paysEsmorzarFestes,
            paysDinarEntrada,
            paysDinarSantJordi,
            paysSoparSantJordi,
            paysDinarTrons
        )

        usersInterface.updateCategoryInformation(category, ageMin..ageMax, price, rightsData, paysData)

        call.respondSuccess()
    }
}
