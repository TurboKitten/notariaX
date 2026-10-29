package com.duoc.notariaplataforma.model

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Usuario::class], version = 1, exportSchema = false)
abstract class NotariaDatabase : RoomDatabase() {
    abstract fun usuarioDao(): UsuarioDao
}