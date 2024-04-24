package com.notesmakers.noteapp.data.auth

import androidx.annotation.StringRes
import com.notesmakers.noteapp.R
import com.notesmakers.noteapp.presentation.auth.login.LoginViewModel

enum class ErrorInputType(@StringRes var nameRes: Int) {
    BLANK_EMAIL(R.string.app_name),
    BLANK_PASSWORD(R.string.app_name),
    WRONG_PASSWORD_OR_EMAIL(R.string.app_name)
}

interface InputError {
    val text: String
    val errors: MutableSet<ErrorInputType>
    fun validate()
    fun hasErrors(): Boolean
    fun onError(): Int?
}

class EmailInputError(override val text: String) : InputError {
    override val errors: MutableSet<ErrorInputType> = mutableSetOf()

    override fun validate() {
        if (text.isBlank()) errors.add(ErrorInputType.BLANK_EMAIL)
    }

    override fun hasErrors(): Boolean = errors.isNotEmpty()

    override fun onError(): Int? = errors.takeIf { it.size > 0 }?.first()?.nameRes
}

class PasswordInputError(override val text: String) : InputError {
    override val errors: MutableSet<ErrorInputType> = mutableSetOf()

    override fun validate() {
        if (text.isBlank()) errors.add(ErrorInputType.BLANK_EMAIL)
    }

    override fun hasErrors(): Boolean = errors.isNotEmpty()

    override fun onError(): Int? = errors.takeIf { it.size > 0 }?.first()?.nameRes
}

class NameInputError(override val text: String) : InputError {
    override val errors: MutableSet<ErrorInputType> = mutableSetOf()

    override fun validate() {
        if (text.isBlank()) errors.add(ErrorInputType.BLANK_EMAIL)
    }

    override fun hasErrors(): Boolean = errors.isNotEmpty()

    override fun onError(): Int? = errors.takeIf { it.size > 0 }?.first()?.nameRes
}

class NicknameInputError(override val text: String) : InputError {
    override val errors: MutableSet<ErrorInputType> = mutableSetOf()

    override fun validate() {
        if (text.isBlank()) errors.add(ErrorInputType.BLANK_EMAIL)
    }

    override fun hasErrors(): Boolean = errors.isNotEmpty()

    override fun onError(): Int? = errors.takeIf { it.size > 0 }?.first()?.nameRes
}

class SurnameInputError(override val text: String) : InputError {
    override val errors: MutableSet<ErrorInputType> = mutableSetOf()

    override fun validate() {
        if (text.isBlank()) errors.add(ErrorInputType.BLANK_EMAIL)
    }

    override fun hasErrors(): Boolean = errors.isNotEmpty()

    override fun onError(): Int? = errors.takeIf { it.size > 0 }?.first()?.nameRes
}