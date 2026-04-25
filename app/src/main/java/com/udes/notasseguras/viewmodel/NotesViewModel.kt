package com.udes.notasseguras.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.udes.notasseguras.security.SecureStorageManager

class NotesViewModel(app: Application) : AndroidViewModel(app) {

    val tokenGuardado = MutableLiveData<String?>(
        SecureStorageManager.obtenerToken(app)
    )
    val mensajeError = MutableLiveData<String>("")

    fun guardarToken(token: String) {
        if (token.isBlank()) {
            mensajeError.value = "El token no puede estar vacío"
            return
        }
        SecureStorageManager.guardarToken(getApplication(), token)
        tokenGuardado.value = "Token guardado (cifrado)"
    }

    fun cerrarSesion() {
        SecureStorageManager.limpiarSesion(getApplication())
        tokenGuardado.value = null
    }
}