package com.arnyminerz.utils.serialization

import org.json.JSONObject

interface JsonSerializable {
    fun toJSON(): JSONObject
}
