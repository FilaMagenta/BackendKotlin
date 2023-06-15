package com.arnyminerz

import com.arnyminerz.database.ServerDatabase
import com.arnyminerz.database.connector.EventsInterface
import com.arnyminerz.database.connector.UsersInterface
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before

abstract class DatabaseTestProto {
    @Before
    fun setInstance() = runBlocking {
        TestingDatabase.Instance.set()
        ServerDatabase.instance<TestingDatabase>().reInit()
    }

    @After
    fun dispose() {
        ServerDatabase.instance.dispose()
    }

    protected val usersInterface: UsersInterface
        get() = ServerDatabase.instance.usersInterface

    protected val eventsInterface: EventsInterface
        get() = ServerDatabase.instance.eventsInterface
}
