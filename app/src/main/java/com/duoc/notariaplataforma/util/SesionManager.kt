package com.duoc.notariaplataforma.util

import android.content.Context
import android.content.SharedPreferences

class SesionManager(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences("sesion_notariax", Context.MODE_PRIVATE)

    fun guardarSesion(correo: String) {
        val editor = prefs.edit()
        editor.putBoolean("sesion_activa", true)
        editor.putString("correo_usuario", correo)
        editor.apply()
    }

    fun cerrarSesion() {
        val editor = prefs.edit()
        editor.putBoolean("sesion_activa", false)
        editor.remove("correo_usuario")
        editor.apply()
    }

    fun haySesionActiva(): Boolean {
        return prefs.getBoolean("sesion_activa", false)
    }

    fun obtenerCorreo(): String {
        return prefs.getString("correo_usuario", "") ?: ""
    }
}