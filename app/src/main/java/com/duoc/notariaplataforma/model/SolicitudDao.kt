package com.duoc.notariaplataforma.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SolicitudDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertarSolicitud(solicitud: Solicitud)

    @Query("SELECT * FROM solicitudes ORDER BY id DESC")
    fun obtenerSolicitudes(): Flow<List<Solicitud>>
}