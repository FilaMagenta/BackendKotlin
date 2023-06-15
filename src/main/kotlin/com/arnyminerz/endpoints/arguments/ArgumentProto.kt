package com.arnyminerz.endpoints.arguments

abstract class ArgumentProto<R : Any, Type : ArgumentType<R>>(
    val name: String,
    val type: Type
)
