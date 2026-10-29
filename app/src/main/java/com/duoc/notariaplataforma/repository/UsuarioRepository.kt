package com.duoc.notariaplataforma.repository

import com.duoc.notariaplataforma.model.Usuario
import com.duoc.notariaplataforma.model.UsuarioDao
import kotlinx.coroutines.flow.Flow

class UsuarioRepository(private val usuarioDao: UsuarioDao) {

    val todosLosUsuarios: Flow<List<Usuario>> = usuarioDao.obtenerTodosLosUsuarios()

    suspend fun registrarUsuario(usuario: Usuario) {
        usuarioDao.insertarUsuario(usuario)
    }

    suspend fun iniciarSesion(correo: String, contrasena: String): Usuario? {
        return usuarioDao.iniciarSesion(correo, contrasena)
    }
}