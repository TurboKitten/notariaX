package com.duoc.notariaplataforma.remote.dto

import com.google.gson.annotations.SerializedName

data class TipoTramiteDto(
    @SerializedName("id") val id: Int? = null,
    @SerializedName("created_at") val createdAt: Long? = null,
    @SerializedName("nombre") val nombre: String = "",
    @SerializedName("descripcion") val descripcion: String? = null,
    @SerializedName("activo") val activo: Boolean? = null
)
