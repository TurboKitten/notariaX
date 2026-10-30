package com.duoc.notariaplataforma.repository

import com.duoc.notariaplataforma.model.Solicitud
import com.duoc.notariaplataforma.model.SolicitudDao
import com.duoc.notariaplataforma.model.Usuario
import com.duoc.notariaplataforma.model.UsuarioDao
import kotlinx.coroutines.flow.Flow

class UsuarioRepository(
    private val usuarioDao: UsuarioDao,
    private val solicitudDao: SolicitudDao
) {

    val todosLosUsuarios: Flow<List<Usuario>> = usuarioDao.obtenerTodosLosUsuarios()
    val todasLasSolicitudes: Flow<List<Solicitud>> = solicitudDao.obtenerSolicitudes()

    suspend fun registrarUsuario(usuario: Usuario) {
        usuarioDao.insertarUsuario(usuario)
    }

    suspend fun iniciarSesion(correo: String, contrasena: String): Usuario? {
        return usuarioDao.iniciarSesion(correo, contrasena)
    }

    suspend fun obtenerUsuarioPorCorreo(correo: String): Usuario? {
        return usuarioDao.obtenerUsuarioPorCorreo(correo)
    }

    suspend fun guardarSolicitud(solicitud: Solicitud) {
        solicitudDao.insertarSolicitud(solicitud)
    }
}