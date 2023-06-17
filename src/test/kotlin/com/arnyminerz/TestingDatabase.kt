package com.arnyminerz

import com.arnyminerz.database.ServerDatabase
import java.io.File

object TestingDatabase : ServerDatabase(
    // databaseType = "h2",
    host = "testing.db",
    mode = DatabaseMode.FILE
    // options = mapOf("DB_CLOSE_DELAY" to "-1")
) {
    object Instance : ServerDatabaseCompanion<TestingDatabase>(TestingDatabase)

    suspend fun reInit() = init()

    override fun dispose() {
        super.dispose()

        if (!File(host).delete()) System.err.println("Could not delete database")
    }
}
