package com.arnyminerz.endpoints.arguments

import com.arnyminerz.errors.Errors

object Arguments {
    val Nif = Argument("nif", Errors.MissingNifBody)
    val Password = Argument("password", Errors.MissingPasswordBody)
    val Name = Argument("name", Errors.MissingNameBody)
    val Surname = Argument("surname", Errors.MissingSurnameBody)
    val Email = Argument("email", Errors.MissingEmailBody)
    val Description = Argument("description", Errors.MissingDescriptionBody)
    val Date = Argument("date", Errors.MissingDateBody)
    val Until = OptionalArgument("until")
    val Reservations = OptionalArgument("reservations")
}
