package com.duoc.notariaplataforma.remote.api

import com.duoc.notariaplataforma.remote.dto.DeleteSolicitudResponse
import com.duoc.notariaplataforma.remote.dto.SolicitudDto
import com.duoc.notariaplataforma.remote.dto.SolicitudRequest
import com.duoc.notariaplataforma.remote.dto.SolicitudUpdateRequest
import com.duoc.notariaplataforma.remote.dto.TipoTramiteDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface XanoApiService {

    @GET("solicitud")
    suspend fun obtenerSolicitudes(
        @Query("usuario_correo") usuarioCorreo: String? = null
    ): List<SolicitudDto>

    @GET("solicitud/{solicitud_id}")
    suspend fun obtenerSolicitudPorId(
        @Path("solicitud_id") solicitudId: Int
    ): SolicitudDto

    @POST("solicitud")
    suspend fun crearSolicitud(
        @Body solicitud: SolicitudRequest
    ): SolicitudDto

    @PATCH("solicitud/{solicitud_id}")
    suspend fun actualizarSolicitud(
        @Path("solicitud_id") solicitudId: Int,
        @Body solicitud: SolicitudUpdateRequest
    ): SolicitudDto

    @DELETE("solicitud/{solicitud_id}")
    suspend fun eliminarSolicitud(
        @Path("solicitud_id") solicitudId: Int
    ): DeleteSolicitudResponse

    @GET("tipo_tramite")
    suspend fun obtenerTiposTramite(): List<TipoTramiteDto>

    @GET("tipo_tramite/{tipo_tramite_id}")
    suspend fun obtenerTipoTramitePorId(
        @Path("tipo_tramite_id") tipoTramiteId: Int
    ): TipoTramiteDto
}
