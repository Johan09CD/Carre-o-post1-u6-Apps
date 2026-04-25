package com.udes.notasseguras.security

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

object SecureStorageManager {

    private const val PREFS_NAME = "secure_notes_prefs"
    private const val KEY_SESSION_TOKEN = "session_token"
    private const val KEY_USER_PIN = "user_pin"

    private fun getPrefs(context: Context): SharedPreferences {
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
        return EncryptedSharedPreferences.create(
            context,
            PREFS_NAME,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    fun guardarToken(context: Context, token: String) {
        getPrefs(context).edit()
            .putString(KEY_SESSION_TOKEN, token).apply()
    }

    fun obtenerToken(context: Context): String? =
        getPrefs(context).getString(KEY_SESSION_TOKEN, null)

    fun guardarPin(context: Context, pin: String) {
        getPrefs(context).edit()
            .putString(KEY_USER_PIN, pin).apply()
    }

    fun verificarPin(context: Context, pinIngresado: String): Boolean =
        getPrefs(context).getString(KEY_USER_PIN, null) == pinIngresado

    fun limpiarSesion(context: Context) {
        getPrefs(context).edit()
            .remove(KEY_SESSION_TOKEN)
            .commit()
        context.deleteSharedPreferences(PREFS_NAME)
    }
}