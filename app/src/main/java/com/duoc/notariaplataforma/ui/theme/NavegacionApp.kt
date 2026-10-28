package com.duoc.notariaplataforma.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun NavegacionApp() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "splash") {
        composable("splash") { SplashScreen(navController) }
        composable("login") { LoginScreen(navController) }
        composable("registro") { RegistroScreen(navController) }
        composable("home") { HomeScreen(navController) }
        composable("solicitud") { SolicitudTramiteScreen(navController) }
    }
}