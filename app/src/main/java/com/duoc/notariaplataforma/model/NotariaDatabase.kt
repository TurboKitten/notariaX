package com.duoc.notariaplataforma.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Usuario::class, Solicitud::class], version = 2, exportSchema = false)
abstract class NotariaDatabase : RoomDatabase() {
    abstract fun usuarioDao(): UsuarioDao
    abstract fun solicitudDao(): SolicitudDao

    companion object {
        @Volatile
        private var INSTANCE: NotariaDatabase? = null

        fun getDatabase(context: Context): NotariaDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NotariaDatabase::class.java,
                    "notaria_db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
