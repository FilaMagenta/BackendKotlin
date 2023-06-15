package com.arnyminerz.utils.serialization

import org.json.JSONObject

interface JsonSerializer<Result> {
    suspend fun fromJson(json: JSONObject): Result
}
