package com.arnyminerz.endpoints.arguments

import com.arnyminerz.errors.Errors

object Arguments {
    val Name = Argument("name", Errors.MissingNameBody)
    val Description = Argument("description", Errors.MissingDescriptionBody)
    val Date = Argument("date", Errors.MissingDateBody)
    val Until = OptionalArgument("until")
    val Reservations = OptionalArgument("reservations")
}
