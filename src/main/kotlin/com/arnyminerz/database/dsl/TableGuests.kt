package com.arnyminerz.database.dsl

import com.arnyminerz.database.dsl.DSLConst.NIF_LENGTH
import com.arnyminerz.database.dsl.DSLConst.PERSON_NAME_LENGTH
import com.arnyminerz.database.dsl.DSLConst.PERSON_SURNAME_LENGTH
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column

object TableGuests : IntIdTable() {
    val name: Column<String> = varchar("name", PERSON_NAME_LENGTH)
    val surname: Column<String> = varchar("surname", PERSON_SURNAME_LENGTH)
    val nif: Column<String> = varchar("nif", NIF_LENGTH)

    val responsible = reference("responsible", Users)
    val table = reference("table", EventTables)
}
