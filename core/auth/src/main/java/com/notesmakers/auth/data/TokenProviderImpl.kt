package com.notesmakers.auth.data

import android.content.Context
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.notesmakers.auth.data.models.SessionToken
import com.notesmakers.auth.domain.TokenProvider
import org.koin.core.annotation.Single

@Single
class TokenProviderImpl(context: Context) : TokenProvider {
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
        "AppPreferences",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    override fun getTokens(): SessionToken {
        val jtwToken = sharedPreferences.getString("jtwToken", "")
        val refreshToken = sharedPreferences.getString("refreshToken", "")
        return SessionToken(token = jtwToken, refreshToken = refreshToken)
    }

    override suspend fun saveTokens(jtwToken: String?, refreshToken: String?) {
        sharedPreferences
            .edit()
            .putString("jtwToken", jtwToken)
            .putString("refreshToken", refreshToken).apply()

    }
}