package com.arnyminerz.database.migration

abstract class DatabaseMigration(val fromVersion: Int, val toVersion: Int) {
    abstract fun migrate()
}
