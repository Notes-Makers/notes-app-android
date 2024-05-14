package com.notesmakers.auth.data

import android.content.Context
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.notesmakers.auth.data.models.UserDetails
import com.notesmakers.auth.domain.UserProvider
import org.koin.core.annotation.Single

@Single
class UserProviderImpl(context: Context) : UserProvider {
    private val spec: KeyGenParameterSpec = KeyGenParameterSpec.Builder(
        MasterKey.DEFAULT_MASTER_KEY_ALIAS,
        KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
    )
        .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
        .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
        .setKeySize(MasterKey.DEFAULT_AES_GCM_MASTER_KEY_SIZE)
        .build()

    private val masterKey = MasterKey.Builder(context)
        .setKeyGenParameterSpec(spec)
        .build()

    private val sharedPreferences = EncryptedSharedPreferences.create(
        context,
        "UserPreferences",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    override fun getUser(): UserDetails {
        val userName = sharedPreferences.getString("userName", "") ?: ""
        val userEmail = sharedPreferences.getString("userEmail", "") ?: ""
        val name = sharedPreferences.getString("name", "") ?: ""
        val surname = sharedPreferences.getString("surname", "") ?: ""
        return UserDetails(
            userName = userName,
            userEmail = userEmail,
            name = name,
            surname = surname
        )
    }

    override suspend fun saveUser(
        userName: String?,
        userEmail: String?,
        name: String?,
        surname: String?
    ) {
        sharedPreferences
            .edit()
            .putString("userName", userName)
            .putString("userEmail", userEmail)
            .putString("name", name)
            .putString("surname", surname).apply()
    }
}