package com.arnyminerz.endpoints.arguments

/**
 * This is thrown by [CalledArgument.getValue] when the execution flow must be stopped. Should simply be ignored, the
 * result has already been sent.
 */
class MissingArgumentException(argument: Argument<*, *>): Exception("Body did not contain any ${argument.name}")
