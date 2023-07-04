package com.arnyminerz.database.migration

import com.arnyminerz.database.ServerDatabase
import com.arnyminerz.database.dsl.DatabaseInfoTable
import com.arnyminerz.database.entity.DatabaseInfo
import com.arnyminerz.database.entity.DatabaseInfo.Companion.VERSION_ID
import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.jetbrains.exposed.sql.SchemaUtils

object MigrationRunner {
    private val migrations = arrayOf<DatabaseMigration>(
        object : DatabaseMigration(0, 1) {
            override fun migrate() {
                println("Creating required schema...")
                for (entity in ServerDatabase.entityClasses)
                    SchemaUtils.create(entity)
            }
        }
    )

    /**
     * Runs database migrations based on the current version.
     */
    fun runMigrations() {
        println("Running migrations...")
        /**
         * The current version of the database, before running migrations. Defaults to 0, which means that there's no
         * database information table.
         */
        val currentVersion: Int = try {
            DatabaseInfo[VERSION_ID]
                .value
                .toInt()
        } catch (ignored: ExposedSQLException) {
            0
        }
        var newVersion: Int = currentVersion
        val migrations = migrations.sortedBy { it.fromVersion }
        for (migration in migrations) {
            if (newVersion >= migration.fromVersion) continue
            println("!!! RUNNING DATABASE MIGRATION (${migration.fromVersion} -> ${migration.toVersion}) !!!")
            migration.migrate()
            newVersion = migration.toVersion
        }
        try {
            println("Updating database version to $newVersion (was $currentVersion)")
            DatabaseInfo[VERSION_ID].value = newVersion.toString()
        } catch (ignored: ExposedSQLException) {
            // Table doesn't exist, create it, and add the row now
            println("DatabaseInfo table doesn't exist, creating...")
            SchemaUtils.create(DatabaseInfoTable)

            println("Adding version info row to DatabaseInfo...")
            DatabaseInfo.new(VERSION_ID) {
                this.value = newVersion.toString()
            }
        }
    }
}
