package com.duoc.notariaplataforma.util

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences

class SesionManager(context: Context) {

    private val prefs: SharedPreferences = context.applicationContext.getSharedPreferences("sesion_notariax", Context.MODE_PRIVATE)

    @SuppressLint("ApplySharedPref")
    fun guardarSesion(correo: String) {
        val editor = prefs.edit()
        editor.putBoolean("sesion_activa", true)
        editor.putString("correo_usuario", correo)
        editor.commit()
    }

    @SuppressLint("ApplySharedPref")
    fun cerrarSesion() {
        val editor = prefs.edit()
        editor.putBoolean("sesion_activa", false)
        editor.remove("correo_usuario")
        editor.commit()
    }

    fun haySesionActiva(): Boolean {
        return prefs.getBoolean("sesion_activa", false)
    }

    fun obtenerCorreo(): String {
        return prefs.getString("correo_usuario", "") ?: ""
    }
}