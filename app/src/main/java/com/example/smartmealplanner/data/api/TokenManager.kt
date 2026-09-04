package com.example.smartmealplanner.data.api

import android.content.Context
import androidx.core.content.edit
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class TokenManager(context: Context) {

    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val sharedPrefs = EncryptedSharedPreferences.create(
        context,
        "secure_auth_prefs",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    private val _tokenFlow = MutableStateFlow(sharedPrefs.getString(TOKEN_KEY, null))
    val tokenFlow: Flow<String?> = _tokenFlow.asStateFlow()

    companion object {
        private const val TOKEN_KEY = "jwt_token"
    }

    fun saveToken(token: String) {
        sharedPrefs.edit {
            putString(TOKEN_KEY, token)
        }
        _tokenFlow.value = token
    }

    fun clearToken() {
        sharedPrefs.edit {
            remove(TOKEN_KEY)
        }
        _tokenFlow.value = null
    }
}
