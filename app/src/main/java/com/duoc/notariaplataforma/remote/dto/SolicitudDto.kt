package com.duoc.notariaplataforma.remote.dto

import com.google.gson.annotations.SerializedName

data class SolicitudDto(
    @SerializedName("id") val id: Int? = null,
    @SerializedName("created_at") val createdAt: Long? = null,
    @SerializedName("nombre_solicitante") val nombreSolicitante: String = "",
    @SerializedName("tipo_tramite") val tipoTramite: String = "",
    @SerializedName("prioridad") val prioridad: String = "",
    @SerializedName("cantidad_copias") val cantidadCopias: Int = 1,
    @SerializedName("estado") val estado: String = "En Proceso",
    @SerializedName("fecha_registro") val fechaRegistro: String = "",
    @SerializedName("usuario_correo") val usuarioCorreo: String? = null
)

data class SolicitudRequest(
    @SerializedName("nombre_solicitante") val nombreSolicitante: String,
    @SerializedName("tipo_tramite") val tipoTramite: String,
    @SerializedName("prioridad") val prioridad: String,
    @SerializedName("cantidad_copias") val cantidadCopias: Int,
    @SerializedName("estado") val estado: String = "En Proceso",
    @SerializedName("fecha_registro") val fechaRegistro: String? = null,
    @SerializedName("usuario_correo") val usuarioCorreo: String? = null
)

data class SolicitudUpdateRequest(
    @SerializedName("nombre_solicitante") val nombreSolicitante: String? = null,
    @SerializedName("tipo_tramite") val tipoTramite: String? = null,
    @SerializedName("prioridad") val prioridad: String? = null,
    @SerializedName("cantidad_copias") val cantidadCopias: Int? = null,
    @SerializedName("estado") val estado: String? = null,
    @SerializedName("fecha_registro") val fechaRegistro: String? = null,
    @SerializedName("usuario_correo") val usuarioCorreo: String? = null
)

data class DeleteSolicitudResponse(
    @SerializedName("deleted") val deleted: Boolean = false,
    @SerializedName("id") val id: Int? = null,
    @SerializedName("record") val record: SolicitudDto? = null
)
