package com.notesmakers.network.data


abstract class NetworkException(
    message: String?,
    cause: Throwable?
) : RuntimeException(
    message ?: cause?.message ?: "Something went wrong",
    cause
)

class CreateNoteException(
    message: String?,
    cause: Throwable?
) : NetworkException(
    message = message,
    cause = cause
)


class GetItemException(
    message: String?,
    cause: Throwable?
) : NetworkException(
    message = message,
    cause = cause
)

class GetItemsInfoException(
    message: String?,
    cause: Throwable?
) : NetworkException(
    message = message,
    cause = cause
)

class GetNoteException(
    message: String?,
    cause: Throwable?
) : NetworkException(
    message = message,
    cause = cause
)