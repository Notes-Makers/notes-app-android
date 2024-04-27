package com.notesmakers.auth.data

import android.content.Context
import androidx.annotation.StringRes
import com.notesmakers.auth.R
import com.notesmakers.auth.domain.AuthErrorMapper
import org.koin.core.annotation.Factory

@Factory
class AuthAuthErrorMapperImpl(private val context: Context) : AuthErrorMapper {
    override fun map(throwable: Throwable): String {
        return when (throwable) {
            is RegisterUserException -> mapException(throwable)
            is LoginException -> mapException(throwable)
            else -> throwable.message ?: getMessage(R.string.error_unknown)
        }
    }

    private fun mapException(throwable: AuthException): String {
        return when (val cause = throwable.cause) {
            is ExtendedValidationError -> "${getMessage(R.string.error_incorrect_data_unknown)} ${cause.cause?.message}"
            else -> throwable.message ?: getMessage(R.string.error_unknown)
        }
    }

    private fun getMessage(@StringRes stringRes: Int) = context.getString(stringRes)
}