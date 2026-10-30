package com.duoc.notariaplataforma.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "solicitudes")
data class Solicitud(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nombreSolicitante: String,
    val tipoTramite: String,
    val prioridad: String,
    val cantidadCopias: Int,
    val estado: String = "En Proceso",
    val fechaRegistro: String = System.currentTimeMillis().toString()
)