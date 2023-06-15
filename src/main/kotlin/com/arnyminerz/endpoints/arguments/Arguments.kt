package com.arnyminerz.endpoints.arguments

import com.arnyminerz.errors.Errors

object Arguments {
    val Nif = Argument("nif", ArgumentTypes.STRING, Errors.MissingNifBody)
    val Password = Argument("password", ArgumentTypes.STRING, Errors.MissingPasswordBody)
    val Name = Argument("name", ArgumentTypes.STRING, Errors.MissingNameBody)
    val Surname = Argument("surname", ArgumentTypes.STRING, Errors.MissingSurnameBody)
    val Email = Argument("email", ArgumentTypes.STRING, Errors.MissingEmailBody)
    val Description = Argument("description", ArgumentTypes.STRING, Errors.MissingDescriptionBody)
    val Date = Argument("date", ArgumentTypes.DATETIME, Errors.MissingDateBody)
    val Until = OptionalArgument("until", ArgumentTypes.DATETIME)
    val Reservations = OptionalArgument("reservations", ArgumentTypes.DATETIME)
    val Price = Argument("price", ArgumentTypes.FLOAT, Errors.MissingPriceBody)
    val Item = OptionalArgument("item", ArgumentTypes.INTEGER)
    val Amount = Argument("amount", ArgumentTypes.INTEGER, Errors.MissingAmountBody)
    val User = Argument("user_id", ArgumentTypes.INTEGER, Errors.MissingUserIdBody)
    val MaxGuests = OptionalArgument("max_guests", ArgumentTypes.INTEGER)
    val Size = OptionalArgument("size", ArgumentTypes.INTEGER)
    val Category = Argument("category", ArgumentTypes.STRING, Errors.MissingCategoryBody)
}
