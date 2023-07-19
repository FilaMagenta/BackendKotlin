package com.arnyminerz.database.entity

import com.arnyminerz.filamagenta.commons.data.types.DataType
import com.arnyminerz.filamagenta.commons.utils.serialization.JsonSerializable
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.id.EntityID

abstract class DataEntity<T : DataType>(id: EntityID<Long>) : LongEntity(id), JsonSerializable {
    abstract fun fill(type: T)
}
