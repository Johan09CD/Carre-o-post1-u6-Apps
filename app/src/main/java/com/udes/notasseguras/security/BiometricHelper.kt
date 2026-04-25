package com.udes.notasseguras.security

import android.content.Context
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity

object BiometricHelper {

    fun biometriaDisponible(context: Context): Boolean {
        val manager = BiometricManager.from(context)
        return manager.canAuthenticate(
            BiometricManager.Authenticators.BIOMETRIC_STRONG
        ) == BiometricManager.BIOMETRIC_SUCCESS
    }

    fun autenticar(
        activity: FragmentActivity,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val executor = ContextCompat.getMainExecutor(activity)
        val callback = object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationSucceeded(
                result: BiometricPrompt.AuthenticationResult
            ) {
                super.onAuthenticationSucceeded(result)
                onSuccess()
            }
            override fun onAuthenticationError(
                code: Int, msg: CharSequence
            ) {
                onError(msg.toString())
            }
            override fun onAuthenticationFailed() {
                onError("Autenticación fallida. Intente de nuevo.")
            }
        }
        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Verificación de identidad")
            .setSubtitle("Usar biometría para acceder a las notas")
            .setNegativeButtonText("Usar PIN")
            .build()
        BiometricPrompt(activity, executor, callback)
            .authenticate(promptInfo)
    }
}