package com.arnyminerz.database.`interface`

import com.arnyminerz.database.ServerDatabase
import com.arnyminerz.database.entity.Event
import com.arnyminerz.database.entity.User
import com.arnyminerz.database.entity.UserAssistance
import com.arnyminerz.database.types.EventType

class EventsInterface(database: ServerDatabase): DataObjectInterface<EventType, Event, Event.Companion>(database, Event.Companion) {
    override fun Event.processExtras(extras: Map<String, String>) { }

    suspend fun confirmAssistance(user: User, event: Event) = database.transaction {
        UserAssistance.new {
            this.user = user
            this.event = event
        }
    }
}
