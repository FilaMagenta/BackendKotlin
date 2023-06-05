package com.arnyminerz.database.types

import com.arnyminerz.utils.serialization.JsonSerializable

abstract class DataType: JsonSerializable {
    override fun toString(): String = toJSON().toString()
}
