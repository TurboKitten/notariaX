package com.duoc.notariaplataforma.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.duoc.notariaplataforma.model.NotariaDatabase
import com.duoc.notariaplataforma.model.Solicitud
import com.duoc.notariaplataforma.model.Usuario
import com.duoc.notariaplataforma.repository.UsuarioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class UsuarioViewModel(application: Application) : AndroidViewModel(application) {

    private val database = Room.databaseBuilder(
        application.applicationContext,
        NotariaDatabase::class.java,
        "notaria_db"
    )
        .fallbackToDestructiveMigration()
        .build()

    private val repository = UsuarioRepository(database.usuarioDao(), database.solicitudDao())

    private val _usuarioLogueado = MutableStateFlow<Usuario?>(null)
    val usuarioLogueado: StateFlow<Usuario?> = _usuarioLogueado.asStateFlow()

    private val _loginExitoso = MutableStateFlow(false)
    val loginExitoso: StateFlow<Boolean> = _loginExitoso.asStateFlow()

    private val _mensajeError = MutableStateFlow("")
    val mensajeError: StateFlow<String> = _mensajeError.asStateFlow()

    val solicitudes = repository.todasLasSolicitudes

    fun registrarUsuario(usuario: Usuario) {
        viewModelScope.launch {
            repository.registrarUsuario(usuario)
        }
    }

    fun iniciarSesion(correo: String, contrasena: String) {
        viewModelScope.launch {
            val usuario = repository.iniciarSesion(correo, contrasena)
            if (usuario != null) {
                _usuarioLogueado.value = usuario
                _loginExitoso.value = true
                _mensajeError.value = ""
            } else {
                _mensajeError.value = "Correo o contraseÃ±a incorrectos"
                _loginExitoso.value = false
            }
        }
    }

    fun guardarSolicitud(solicitud: Solicitud) {
        viewModelScope.launch {
            repository.guardarSolicitud(solicitud)
        }
    }

    fun cerrarSesion() {
        _usuarioLogueado.value = null
        _loginExitoso.value = false
        _mensajeError.value = ""
    }

    fun limpiarMensaje() {
        _mensajeError.value = ""
    }
}