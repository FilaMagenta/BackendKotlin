package com.arnyminerz.endpoints.transactions

import com.arnyminerz.database.types.TransactionType
import com.arnyminerz.endpoints.arguments.Arguments
import com.arnyminerz.endpoints.arguments.called
import com.arnyminerz.endpoints.arguments.calledOptional
import com.arnyminerz.endpoints.protos.AuthenticatedEndpoint
import com.arnyminerz.errors.Errors
import com.arnyminerz.utils.respondFailure
import com.arnyminerz.utils.respondSuccess
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.call
import io.ktor.util.pipeline.PipelineContext

object NewTransactionEndpoint: AuthenticatedEndpoint {
    override suspend fun PipelineContext<*, ApplicationCall>.endpoint(nif: String) {
        val amount by called { Arguments.Amount }
        val pricePerUnit by called { Arguments.Price }
        val description by called { Arguments.Description }
        val date by called { Arguments.Date }
        val itemId by calledOptional { Arguments.Item }

        val user = usersInterface.findWithNif(nif) { it }
            ?: return call.respondFailure(Errors.NifNotFound)
        val item = itemId?.let { id ->
            inventoryInterface.get(id) { it }
        }

        val transaction = TransactionType(date, amount, pricePerUnit, description, user, item)
        transactionsInterface.new(transaction)

        call.respondSuccess(HttpStatusCode.Created)
    }
}
