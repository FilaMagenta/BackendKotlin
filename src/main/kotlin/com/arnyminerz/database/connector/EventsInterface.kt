package com.arnyminerz.database.connector

import com.arnyminerz.database.ServerDatabase
import com.arnyminerz.database.dsl.EventPricesTable
import com.arnyminerz.database.dsl.EventTables
import com.arnyminerz.database.dsl.TableMembers
import com.arnyminerz.database.dsl.UserAssistances
import com.arnyminerz.database.entity.Event
import com.arnyminerz.database.entity.EventPrice
import com.arnyminerz.database.entity.EventTable
import com.arnyminerz.database.entity.TableMember
import com.arnyminerz.database.entity.User
import com.arnyminerz.database.entity.UserAssistance
import com.arnyminerz.filamagenta.commons.data.Category
import com.arnyminerz.filamagenta.commons.data.types.EventPriceType
import com.arnyminerz.filamagenta.commons.data.types.EventType
import java.time.ZonedDateTime
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.and

class EventsInterface(database: ServerDatabase) : DataObjectInterface<EventType, Event, Event.Companion>(
    database,
    Event.Companion
) {

    suspend fun <Result> getTable(id: Long, eventId: Long, block: (table: EventTable?) -> Result) =
        database.transaction {
            EventTable.find {
                (EventTables.id eq id) and (EventTables.event eq eventId)
            }.singleOrNull().let(block)
        }

    suspend fun <Result> getMemberTable(memberId: Long, block: suspend (table: TableMember?) -> Result) =
        database.transaction {
            TableMember.find {
                TableMembers.user eq memberId
            }.singleOrNull().let { block(it) }
        }

    suspend fun <Result> getResponsibleTable(
        responsibleId: Long,
        eventId: Long,
        block: suspend (table: EventTable?) -> Result
    ) = database.transaction {
        EventTable.find {
            (EventTables.responsible eq responsibleId) and (EventTables.event eq eventId)
        }.singleOrNull().let { block(it) }
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
        } else {
            false
        }
    }

    suspend fun cancelAssistance(user: User, event: Event) = database.transaction {
        val assistance = findAssistance(user, event)
        assistance?.delete()
    }

    suspend fun userAssists(user: User, event: Event) = database.transaction {
        findAssistance(user, event) != null
    }

    suspend fun createTable(responsible: User, event: Event) = database.transaction {
        val existingTable = EventTable.find {
            (EventTables.event eq event.id) and (EventTables.responsible eq responsible.id)
        }.singleOrNull()
        if (existingTable != null) {
            false
        } else {
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
        if (alreadyInTable != null) {
            false
        } else {
            TableMember.new {
                this.user = user
                this.table = table
            }
            true
        }
    }

    suspend fun deleteTable(user: User, table: EventTable) = database.transaction {
        println("Deleting table...")

        val alreadyInTable = EventTable.find {
            (EventTables.id eq table.id) and (EventTables.responsible eq user.id)
        }.singleOrNull()
        if (alreadyInTable != null) {
            // Delete all associated members
            TableMember.find { TableMembers.table eq table.id }
                .forEach { it.delete() }

            alreadyInTable.delete()
            true
        } else {
            false
        }
    }

    suspend fun leaveTable(user: User, table: TableMember) = database.transaction {
        val alreadyInTable = TableMember.find {
            (TableMembers.table eq table.id) and (TableMembers.user eq user.id)
        }.singleOrNull()
        if (alreadyInTable != null) {
            alreadyInTable.delete()
            true
        } else {
            false
        }
    }

    /**
     * Sets the price for a specific event and category.
     *
     * @param event The event for which the price will be set.
     * @param category The category for which the price will be set.
     * @param price The price to set for the event and category.
     *
     * @throws IllegalStateException if the event already has a price assigned for the given category.
     * @throws NullPointerException if the [event] could not be found.
     */
    suspend fun setEventPrice(event: Event, category: Category, price: Double) {
        database.transaction {
            // First check if the combination is already present
            val alreadyPriced = EventPrice.find {
                (EventPricesTable.category eq category.name) and (EventPricesTable.event eq event.id)
            }
            check(alreadyPriced.empty()) { "The event ${event.id} already has a price assigned for $category" }

            // Otherwise, add the new price
            EventPrice.new {
                val eventPrice = EventPriceType(
                    0,
                    ZonedDateTime.now(),
                    price,
                    category,
                    event.id.value,
                )
                fill(eventPrice)
            }
        }
    }
}
