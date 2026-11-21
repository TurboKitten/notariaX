package com.duoc.notariaplataforma.repository

import com.duoc.notariaplataforma.model.Usuario
import com.duoc.notariaplataforma.model.UsuarioDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class UsuarioRepositoryTest {

    private class FakeUsuarioDao : UsuarioDao {
        val usuariosInsertados = mutableListOf<Usuario>()
        private val usuarios = mutableListOf(
            Usuario(id = 1, nombre = "Diego", apellido = "Garrido", rut = "11.111.111-1", correo = "diego@correo.cl", telefono = "123456789", contrasena = "123456")
        )

        override suspend fun insertarUsuario(usuario: Usuario) {
            usuariosInsertados.add(usuario)
            usuarios.add(usuario)
        }

        override suspend fun iniciarSesion(correo: String, contrasena: String): Usuario? {
            return usuarios.firstOrNull { it.correo == correo && it.contrasena == contrasena }
        }

        override suspend fun obtenerUsuarioPorCorreo(correo: String): Usuario? {
            return usuarios.firstOrNull { it.correo == correo }
        }

        override fun obtenerTodosLosUsuarios(): Flow<List<Usuario>> = flowOf(usuarios)
    }

    @Test
    fun iniciarSesion_conCredencialesValidas_retornaUsuario() = runTest {
        val dao = FakeUsuarioDao()
        val repository = UsuarioRepository(dao)

        val resultado = repository.iniciarSesion("diego@correo.cl", "123456")

        assertEquals("Diego", resultado?.nombre)
        assertEquals("diego@correo.cl", resultado?.correo)
    }

    @Test
    fun iniciarSesion_conCredencialesInvalidas_retornaNull() = runTest {
        val dao = FakeUsuarioDao()
        val repository = UsuarioRepository(dao)

        val resultado = repository.iniciarSesion("diego@correo.cl", "mala")

        assertNull(resultado)
    }

    @Test
    fun registrarUsuario_guardaUsuarioEnDao() = runTest {
        val dao = FakeUsuarioDao()
        val repository = UsuarioRepository(dao)
        val nuevoUsuario = Usuario(nombre = "Matias", apellido = "Duncker", rut = "22.222.222-2", correo = "matias@correo.cl", telefono = "987654321", contrasena = "abc123")

        repository.registrarUsuario(nuevoUsuario)

        assertTrue(dao.usuariosInsertados.contains(nuevoUsuario))
        assertEquals(nuevoUsuario, repository.obtenerUsuarioPorCorreo("matias@correo.cl"))
    }
}