package com.duoc.notariaplataforma.ui

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.duoc.notariaplataforma.viewmodel.UsuarioViewModel

@Composable
fun NavegacionApp() {
    val navController = rememberNavController()
    val usuarioViewModel: UsuarioViewModel = viewModel()

    NavHost(navController = navController, startDestination = "splash") {
        composable("splash") { SplashScreen(navController) }
        composable("login") { LoginScreen(navController, usuarioViewModel) }
        composable("registro") { RegistroScreen(navController, usuarioViewModel) }
        composable(
            route = "home?mostrarBienvenida={mostrarBienvenida}",
            arguments = listOf(navArgument("mostrarBienvenida") { type = NavType.BoolType; defaultValue = false })
        ) { backStackEntry ->
            val mostrarBienvenida = backStackEntry.arguments?.getBoolean("mostrarBienvenida") ?: false
            HomeScreen(navController, usuarioViewModel, mostrarBienvenidaInicial = mostrarBienvenida)
        }
        composable("solicitud") { SolicitudTramiteScreen(navController, usuarioViewModel) }
        composable("misSolicitudes") { MisSolicitudesScreen(navController, usuarioViewModel) }
    }
}