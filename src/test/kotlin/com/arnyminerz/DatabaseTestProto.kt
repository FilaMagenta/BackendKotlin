package com.arnyminerz

import com.arnyminerz.database.ServerDatabase
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
}