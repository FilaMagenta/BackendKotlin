package com.arnyminerz.endpoints.transactions

import com.arnyminerz.database.entity.User
import com.arnyminerz.endpoints.arguments.Arguments
import com.arnyminerz.endpoints.arguments.called
import com.arnyminerz.endpoints.arguments.calledOptional
import com.arnyminerz.endpoints.protos.AuthenticatedEndpoint
import com.arnyminerz.filamagenta.commons.data.security.permissions.Permissions
import com.arnyminerz.filamagenta.commons.data.types.TransactionType
import com.arnyminerz.filamagenta.commons.errors.Errors
import com.arnyminerz.utils.respondFailure
import com.arnyminerz.utils.respondSuccess
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.call
import io.ktor.util.pipeline.PipelineContext
import java.time.ZonedDateTime

object NewTransactionEndpoint : AuthenticatedEndpoint(Permissions.Transactions.Create) {
    override suspend fun PipelineContext<*, ApplicationCall>.endpoint(user: User) {
        val amount by called { Arguments.Amount }
        val pricePerUnit by called { Arguments.Price }
        val description by called { Arguments.Description }
        val date by called { Arguments.Date }
        val itemId by calledOptional { Arguments.Item }
        val userId by called { Arguments.User }

        val transactionUser = usersInterface.get(userId) { it } ?: return call.respondFailure(Errors.UserNotFound)

        val item = itemId?.let { id ->
            inventoryInterface.get(id) { it }
        }

        val timestamp = ZonedDateTime.now()
        val transaction = TransactionType(
            timestamp,
            date,
            amount,
            pricePerUnit,
            description,
            transactionUser.id.value,
            item?.id?.value
        )
        transactionsInterface.new(transaction)

        call.respondSuccess(HttpStatusCode.Created)
    }
}
