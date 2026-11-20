package com.duoc.notariaplataforma.repository

import com.duoc.notariaplataforma.remote.api.RetrofitClient
import com.duoc.notariaplataforma.remote.api.XanoApiService
import com.duoc.notariaplataforma.remote.dto.SolicitudDto
import com.duoc.notariaplataforma.remote.dto.SolicitudRequest
import com.duoc.notariaplataforma.remote.dto.SolicitudUpdateRequest
import com.duoc.notariaplataforma.remote.dto.TipoTramiteDto

class SolicitudRepository(
    private val api: XanoApiService = RetrofitClient.api
) {

    suspend fun obtenerSolicitudesRemotas(usuarioCorreo: String? = null): List<SolicitudDto> {
        return api.obtenerSolicitudes(usuarioCorreo)
    }

    suspend fun crearSolicitudRemota(request: SolicitudRequest): SolicitudDto {
        return api.crearSolicitud(request)
    }

    suspend fun obtenerSolicitudRemotaPorId(id: Int): SolicitudDto {
        return api.obtenerSolicitudPorId(id)
    }

    suspend fun actualizarSolicitudRemota(id: Int, request: SolicitudUpdateRequest): SolicitudDto {
        return api.actualizarSolicitud(id, request)
    }

    suspend fun eliminarSolicitudRemota(id: Int) {
        api.eliminarSolicitud(id)
    }

    suspend fun obtenerTiposTramiteRemotos(): List<TipoTramiteDto> {
        return api.obtenerTiposTramite()
    }
}
