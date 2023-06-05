package com.arnyminerz.database.`interface`

import com.arnyminerz.database.ServerDatabase
import com.arnyminerz.database.dsl.UserAssistances
import com.arnyminerz.database.dsl.UserAssistances.entityId
import com.arnyminerz.database.entity.Event
import com.arnyminerz.database.entity.User
import com.arnyminerz.database.entity.UserAssistance
import com.arnyminerz.database.types.EventType
import org.jetbrains.exposed.sql.and

class EventsInterface(database: ServerDatabase): DataObjectInterface<EventType, Event, Event.Companion>(database, Event.Companion) {
    override fun Event.processExtras(extras: Map<String, String>) { }

    suspend fun confirmAssistance(user: User, event: Event) = database.transaction {
        UserAssistance.new {
            this.user = user
            this.event = event
        }
    }

    suspend fun cancelAssistance(user: User, event: Event) = database.transaction {
        val assistance = UserAssistance.find {
            (UserAssistances.event.entityId() eq event.id) and (UserAssistances.user.entityId() eq user.id)
        }.singleOrNull()
        assistance?.delete()
    }
}
