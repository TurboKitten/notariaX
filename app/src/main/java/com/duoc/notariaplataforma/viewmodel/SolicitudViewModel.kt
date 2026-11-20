package com.duoc.notariaplataforma.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.duoc.notariaplataforma.model.Solicitud
import com.duoc.notariaplataforma.remote.dto.SolicitudDto
import com.duoc.notariaplataforma.remote.dto.SolicitudRequest
import com.duoc.notariaplataforma.remote.dto.SolicitudUpdateRequest
import com.duoc.notariaplataforma.remote.dto.TipoTramiteDto
import com.duoc.notariaplataforma.repository.SolicitudRepository
import com.duoc.notariaplataforma.util.SesionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SolicitudViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = SolicitudRepository()
    private val sesionManager = SesionManager(application)

    private val _solicitudes = MutableStateFlow<List<Solicitud>>(emptyList())
    val solicitudes: StateFlow<List<Solicitud>> = _solicitudes.asStateFlow()

    private val _tiposTramite = MutableStateFlow<List<TipoTramiteDto>>(emptyList())
    val tiposTramite: StateFlow<List<TipoTramiteDto>> = _tiposTramite.asStateFlow()

    private val _cargandoSolicitudes = MutableStateFlow(false)
    val cargandoSolicitudes: StateFlow<Boolean> = _cargandoSolicitudes.asStateFlow()

    private val _errorSolicitudes = MutableStateFlow("")
    val errorSolicitudes: StateFlow<String> = _errorSolicitudes.asStateFlow()

    private val _operacionSolicitudExitosa = MutableStateFlow(false)
    val operacionSolicitudExitosa: StateFlow<Boolean> = _operacionSolicitudExitosa.asStateFlow()

    private val _mensajeOperacionSolicitud = MutableStateFlow("")
    val mensajeOperacionSolicitud: StateFlow<String> = _mensajeOperacionSolicitud.asStateFlow()

    fun cargarSolicitudes(usuarioCorreo: String? = null) {
        viewModelScope.launch {
            _cargandoSolicitudes.value = true
            _errorSolicitudes.value = ""
            try {
                val correoFiltro = usuarioCorreo ?: sesionManager.obtenerCorreo().ifBlank { null }
                val remotas = repository.obtenerSolicitudesRemotas(correoFiltro)
                _solicitudes.value = remotas.map { it.toSolicitud() }
            } catch (e: Exception) {
                _errorSolicitudes.value = "No se pudieron cargar las solicitudes: ${e.message ?: "error de conexión"}"
            } finally {
                _cargandoSolicitudes.value = false
            }
        }
    }

    fun cargarTiposTramite() {
        viewModelScope.launch {
            try {
                val tipos = repository.obtenerTiposTramiteRemotos()
                _tiposTramite.value = tipos.filter { it.activo != false }
            } catch (_: Exception) {
                // Si falla, la UI usará la lista por defecto
            }
        }
    }

    fun guardarSolicitud(solicitud: Solicitud) {
        viewModelScope.launch {
            try {
                val correo = sesionManager.obtenerCorreo().ifBlank { null }
                val request = SolicitudRequest(
                    nombreSolicitante = solicitud.nombreSolicitante,
                    tipoTramite = solicitud.tipoTramite,
                    prioridad = solicitud.prioridad,
                    cantidadCopias = solicitud.cantidadCopias,
                    estado = solicitud.estado,
                    fechaRegistro = solicitud.fechaRegistro,
                    usuarioCorreo = correo
                )
                repository.crearSolicitudRemota(request)
                _operacionSolicitudExitosa.value = true
                _mensajeOperacionSolicitud.value = "Solicitud creada correctamente"
                cargarSolicitudes(correo)
            } catch (e: Exception) {
                _operacionSolicitudExitosa.value = false
                _mensajeOperacionSolicitud.value = "Error al crear la solicitud: ${e.message ?: "error de conexión"}"
            }
        }
    }

    fun actualizarEstadoSolicitud(solicitudId: Int, nuevoEstado: String) {
        viewModelScope.launch {
            try {
                repository.actualizarSolicitudRemota(
                    solicitudId,
                    SolicitudUpdateRequest(estado = nuevoEstado)
                )
                _operacionSolicitudExitosa.value = true
                _mensajeOperacionSolicitud.value = "Solicitud actualizada correctamente"
                cargarSolicitudes()
            } catch (e: Exception) {
                _operacionSolicitudExitosa.value = false
                _mensajeOperacionSolicitud.value = "Error al actualizar la solicitud: ${e.message ?: "error de conexión"}"
            }
        }
    }

    fun actualizarSolicitud(
        solicitudId: Int,
        nombreSolicitante: String,
        tipoTramite: String,
        prioridad: String,
        cantidadCopias: Int,
        estado: String
    ) {
        viewModelScope.launch {
            try {
                repository.actualizarSolicitudRemota(
                    solicitudId,
                    SolicitudUpdateRequest(
                        nombreSolicitante = nombreSolicitante,
                        tipoTramite = tipoTramite,
                        prioridad = prioridad,
                        cantidadCopias = cantidadCopias,
                        estado = estado
                    )
                )
                _operacionSolicitudExitosa.value = true
                _mensajeOperacionSolicitud.value = "Solicitud actualizada correctamente"
                cargarSolicitudes()
            } catch (e: Exception) {
                _operacionSolicitudExitosa.value = false
                _mensajeOperacionSolicitud.value = "Error al actualizar la solicitud: ${e.message ?: "error de conexión"}"
            }
        }
    }

    fun eliminarSolicitud(solicitudId: Int) {
        viewModelScope.launch {
            try {
                repository.eliminarSolicitudRemota(solicitudId)
                _operacionSolicitudExitosa.value = true
                _mensajeOperacionSolicitud.value = "Solicitud eliminada correctamente"
                cargarSolicitudes()
            } catch (e: Exception) {
                _operacionSolicitudExitosa.value = false
                _mensajeOperacionSolicitud.value = "Error al eliminar la solicitud: ${e.message ?: "error de conexión"}"
            }
        }
    }

    fun limpiarEstadoOperacionSolicitud() {
        _operacionSolicitudExitosa.value = false
        _mensajeOperacionSolicitud.value = ""
    }

    fun limpiarSolicitudes() {
        _solicitudes.value = emptyList()
        _errorSolicitudes.value = ""
        _mensajeOperacionSolicitud.value = ""
        _operacionSolicitudExitosa.value = false
    }

    private fun SolicitudDto.toSolicitud(): Solicitud {
        return Solicitud(
            id = this.id ?: 0,
            nombreSolicitante = this.nombreSolicitante,
            tipoTramite = this.tipoTramite,
            prioridad = this.prioridad,
            cantidadCopias = this.cantidadCopias,
            estado = this.estado,
            fechaRegistro = this.fechaRegistro
        )
    }
}
