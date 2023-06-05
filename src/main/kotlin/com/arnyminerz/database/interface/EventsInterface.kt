package com.arnyminerz.database.`interface`

import com.arnyminerz.database.ServerDatabase
import com.arnyminerz.database.dsl.UserAssistances
import com.arnyminerz.database.entity.Event
import com.arnyminerz.database.entity.User
import com.arnyminerz.database.entity.UserAssistance
import com.arnyminerz.database.types.EventType
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.and

class EventsInterface(database: ServerDatabase) :
    DataObjectInterface<EventType, Event, Event.Companion>(database, Event.Companion) {
    override fun Event.processExtras(extras: Map<String, String>) {}

    private fun Transaction.findAssistance(user: User, event: Event) = UserAssistance.find {
        (UserAssistances.event eq event.id) and (UserAssistances.user eq user.id)
    }.singleOrNull()

    suspend fun confirmAssistance(user: User, event: Event) = database.transaction {
        if (findAssistance(user, event) == null) {
            UserAssistance.new {
                this.user = user
                this.event = event
            }
            true
        } else
            false
    }

    suspend fun cancelAssistance(user: User, event: Event) = database.transaction {
        val assistance = findAssistance(user, event)
        assistance?.delete()
    }
}
