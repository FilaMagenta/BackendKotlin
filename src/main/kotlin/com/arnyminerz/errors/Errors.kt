package com.arnyminerz.errors

import io.ktor.http.HttpStatusCode

object Errors {
    val EndpointNotFound = Error(1, "Endpoint not found", HttpStatusCode.NotFound)
    val Internal = Error(2, "Internal server exception", HttpStatusCode.InternalServerError)
    val EventNotFound = Error(3, "Could not find event.", HttpStatusCode.BadRequest)
    val TableNotFound = Error(4, "Could not find table.", HttpStatusCode.BadRequest)

    val MissingNifBody = Error(10, "Request doesn't contain the required 'nif' argument.", HttpStatusCode.BadRequest)
    val MissingNameBody = Error(11, "Request doesn't contain the required 'name' argument.", HttpStatusCode.BadRequest)
    val MissingSurnameBody = Error(12, "Request doesn't contain the required 'surname' argument.", HttpStatusCode.BadRequest)
    val MissingEmailBody = Error(13, "Request doesn't contain the required 'email' argument.", HttpStatusCode.BadRequest)
    val MissingPasswordBody = Error(14, "Request doesn't contain the required 'password' argument.", HttpStatusCode.BadRequest)
    val MissingDescriptionBody = Error(15, "Request doesn't contain the required 'description' argument.", HttpStatusCode.BadRequest)
    val MissingDateBody = Error(16, "Request doesn't contain the required 'date' argument.", HttpStatusCode.BadRequest)
    val MissingPriceBody = Error(17, "Request doesn't contain the required 'price' argument.", HttpStatusCode.BadRequest)
    val MissingAmountBody = Error(18, "Request doesn't contain the required 'amount' argument.", HttpStatusCode.BadRequest)

    val NifInvalid = Error(20, "The given NIF is not valid.", HttpStatusCode.BadRequest)
    val EmailInvalid = Error(21, "The given email is not valid.", HttpStatusCode.BadRequest)

    val NifAlreadyRegistered = Error(30, "The given NIF is already registered in the database.", HttpStatusCode.BadRequest)

    val Unauthorized = Error(40, "Token is not valid or has expired.", HttpStatusCode.Unauthorized)
    val NifNotFound = Error(41, "The given NIF was not found in the database.", HttpStatusCode.BadRequest)
    val WrongPassword = Error(42, "Wrong NIF or password.", HttpStatusCode.BadRequest)
    val MissingPermission = Error(43, "You are not authorized to perform this operation", HttpStatusCode.Forbidden)

    val AssistanceAlreadyConfirmed = Error(50, "Assistance already confirmed.", HttpStatusCode.BadRequest)
    val UserAlreadyInTable = Error(51, "You are already in a table.", HttpStatusCode.BadRequest)
    val UserNotInTable = Error(52, "The user is not in the table.", HttpStatusCode.BadRequest)
}
