package com.arnyminerz.database.`interface`

import com.arnyminerz.database.ServerDatabase
import com.arnyminerz.database.dsl.EventTables
import com.arnyminerz.database.dsl.TableMembers
import com.arnyminerz.database.dsl.UserAssistances
import com.arnyminerz.database.entity.Event
import com.arnyminerz.database.entity.EventTable
import com.arnyminerz.database.entity.TableMember
import com.arnyminerz.database.entity.User
import com.arnyminerz.database.entity.UserAssistance
import com.arnyminerz.database.types.EventType
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.and

class EventsInterface(database: ServerDatabase) :
    DataObjectInterface<EventType, Event, Event.Companion>(database, Event.Companion) {
    override fun Event.processExtras(extras: Map<String, String>) {}

    suspend fun <Result> getTable(id: Int, eventId: Int, block: (table: EventTable?) -> Result) = database.transaction {
        EventTable.find {
            (EventTables.id eq id) and (EventTables.event eq eventId)
        }.singleOrNull().let(block)
    }

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

    suspend fun createTable(responsible: User, event: Event) = database.transaction {
        val existingTable = EventTable.find {
            (EventTables.event eq event.id) and (EventTables.responsible eq responsible.id)
        }.singleOrNull()
        if (existingTable != null)
            false
        else {
            EventTable.new {
                this.responsible = responsible
                this.event = event
            }
            true
        }
    }

    suspend fun joinTable(user: User, table: EventTable) = database.transaction {
        val alreadyInTable = TableMember.find {
            (TableMembers.table eq table.id) and (TableMembers.user eq user.id)
        }.singleOrNull()
        if (alreadyInTable != null)
            false
        else {
            TableMember.new {
                this.user = user
                this.table = table
            }
            true
        }
    }
}
