package com.arnyminerz

import com.arnyminerz.database.ServerDatabase
import java.io.File

object TestingDatabase: ServerDatabase(
    // databaseType = "h2",
    path = "testing.db",
    // options = mapOf("DB_CLOSE_DELAY" to "-1")
) {
    object Instance: ServerDatabaseCompanion<TestingDatabase>(TestingDatabase)

    suspend fun reInit() = init()

    override fun dispose() {
        super.dispose()

        if (!File(path).delete()) System.err.println("Could not delete database")
    }
}
