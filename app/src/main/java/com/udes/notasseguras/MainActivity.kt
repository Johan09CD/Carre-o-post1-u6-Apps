package com.udes.notasseguras

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.udes.notasseguras.security.BiometricHelper
import com.udes.notasseguras.security.SecureStorageManager
import com.udes.notasseguras.viewmodel.NotesViewModel

class MainActivity : AppCompatActivity() {

    private val viewModel: NotesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val etToken    = findViewById<EditText>(R.id.etToken)
        val btnGuardar = findViewById<Button>(R.id.btnGuardar)
        val btnVer     = findViewById<Button>(R.id.btnVer)
        val btnLogout  = findViewById<Button>(R.id.btnLogout)
        val tvEstado   = findViewById<TextView>(R.id.tvEstado)

        viewModel.tokenGuardado.observe(this) { token ->
            tvEstado.text = token ?: "Sin sesión activa"
        }

        viewModel.mensajeError.observe(this) { error ->
            if (error.isNotEmpty()) tvEstado.text = error
        }

        btnGuardar.setOnClickListener {
            viewModel.guardarToken(etToken.text.toString())
            etToken.text.clear()
        }

        btnVer.setOnClickListener {
            if (BiometricHelper.biometriaDisponible(this)) {
                BiometricHelper.autenticar(
                    activity = this,
                    onSuccess = {
                        val token = SecureStorageManager.obtenerToken(this@MainActivity)
                        tvEstado.text = token ?: "Sin token guardado"
                    },
                    onError = { msg ->
                        tvEstado.text = "Error: $msg"
                    }
                )
            } else {
                val token = SecureStorageManager.obtenerToken(this)
                tvEstado.text = token ?: "Sin token guardado"
            }
        }

        btnLogout.setOnClickListener {
            viewModel.cerrarSesion()
        }
    }
}