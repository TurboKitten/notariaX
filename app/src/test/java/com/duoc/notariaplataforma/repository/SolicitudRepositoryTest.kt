package com.duoc.notariaplataforma.repository

import com.duoc.notariaplataforma.remote.api.XanoApiService
import com.duoc.notariaplataforma.remote.dto.SolicitudRequest
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SolicitudRepositoryTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var repository: SolicitudRepository

    @Before
    fun setUp() {
        mockWebServer = MockWebServer()
        mockWebServer.start()
        val api = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(XanoApiService::class.java)
        repository = SolicitudRepository(api)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun obtenerSolicitudesRemotas_filtraPorCorreoYRetornaLista() = runTest {
        val body = """
            [
              {
                "id": 1,
                "created_at": 1795191315330,
                "nombre_solicitante": "Diego",
                "tipo_tramite": "Poder simple",
                "prioridad": "Alta",
                "cantidad_copias": 1,
                "estado": "En Proceso",
                "fecha_registro": "Hoy",
                "usuario_correo": "diego@correo.cl"
              }
            ]
        """.trimIndent()
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .addHeader("Content-Type", "application/json")
                .setBody(body)
        )

        val resultado = repository.obtenerSolicitudesRemotas("diego@correo.cl")

        val request = mockWebServer.takeRequest()
        assertEquals("/solicitud?usuario_correo=diego%40correo.cl", request.path)
        assertEquals(1, resultado.size)
        assertEquals("Diego", resultado[0].nombreSolicitante)
        assertEquals("Poder simple", resultado[0].tipoTramite)
    }

    @Test
    fun crearSolicitudRemota_enviaRequestYRetornaSolicitudCreada() = runTest {
        val responseBody = """
            {
              "id": 10,
              "created_at": 1795191315330,
              "nombre_solicitante": "Diego",
              "tipo_tramite": "Poder simple",
              "prioridad": "Alta",
              "cantidad_copias": 1,
              "estado": "En Proceso",
              "fecha_registro": "Hoy",
              "usuario_correo": "diego@correo.cl"
            }
        """.trimIndent()
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .addHeader("Content-Type", "application/json")
                .setBody(responseBody)
        )

        val requestBody = SolicitudRequest(
            nombreSolicitante = "Diego",
            tipoTramite = "Poder simple",
            prioridad = "Alta",
            cantidadCopias = 1,
            estado = "En Proceso",
            fechaRegistro = "Hoy",
            usuarioCorreo = "diego@correo.cl"
        )

        val resultado = repository.crearSolicitudRemota(requestBody)

        val recorded = mockWebServer.takeRequest()
        assertEquals("/solicitud", recorded.path)
        assertEquals("POST", recorded.method)
        assertEquals(10, resultado.id)
        assertEquals("Poder simple", resultado.tipoTramite)
    }
}