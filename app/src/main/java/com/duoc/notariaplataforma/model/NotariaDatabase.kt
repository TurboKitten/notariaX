package com.duoc.notariaplataforma.model

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Usuario::class, Solicitud::class], version = 2, exportSchema = false)
abstract class NotariaDatabase : RoomDatabase() {
    abstract fun usuarioDao(): UsuarioDao
    abstract fun solicitudDao(): SolicitudDao
}