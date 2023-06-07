package com.arnyminerz.endpoints.transactions

import com.arnyminerz.endpoints.protos.AuthenticatedEndpoint
import com.arnyminerz.errors.Errors
import com.arnyminerz.utils.respondFailure
import com.arnyminerz.utils.respondSuccess
import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.call
import io.ktor.util.pipeline.PipelineContext
import org.json.JSONArray
import org.json.JSONObject

object GetTransactionsEndpoint: AuthenticatedEndpoint {
    override suspend fun PipelineContext<*, ApplicationCall>.endpoint(nif: String) {
        val user = usersInterface.findWithNif(nif) { it }
            ?: return call.respondFailure(Errors.NifNotFound)

        val transactions = JSONArray()
        transactionsInterface.findForUser(user) { list ->
            for (item in list)
                transactions.put(item.toJSON())
        }

        call.respondSuccess(
            JSONObject().apply {
                put("transactions", transactions)
            }
        )
    }
}