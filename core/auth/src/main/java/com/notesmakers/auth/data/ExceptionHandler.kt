package com.notesmakers.auth.data

import java.lang.RuntimeException

abstract class AuthException(
    message: String?,
    cause: Throwable?
) : RuntimeException(
    message ?: cause?.message ?: "Something went wrong",
    cause
)

class LoginException(
    message: String?,
    cause: Throwable?
) : AuthException(
    message = message,
    cause = cause
)
class RefreshTokenException(
    message: String?,
    cause: Throwable?
) : AuthException(
    message = message,
    cause = cause
)
class LogoutException(
    message: String?,
    cause: Throwable?
) : AuthException(
    message = message,
    cause = cause
)

class RegisterUserException(
    message: String?,
    cause: Throwable?
) : AuthException(
    message = message,
    cause = cause
)
class ExtendedValidationError(
    message: String?,
    cause: Throwable?
) : AuthException(
    message = message,
    cause = cause
)