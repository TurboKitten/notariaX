package com.duoc.notariaplataforma.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.duoc.notariaplataforma.R
import com.duoc.notariaplataforma.util.SesionManager
import com.duoc.notariaplataforma.viewmodel.UsuarioViewModel
@Composable
fun LoginScreen(navController: NavController, viewModel: UsuarioViewModel) {
    var correo by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var mostrarModalError by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val sesionManager = remember { SesionManager(context) }

    val loginExitoso by viewModel.loginExitoso.collectAsState()
    val mensajeError by viewModel.mensajeError.collectAsState()

    LaunchedEffect(loginExitoso) {
        if (loginExitoso) {
            sesionManager.guardarSesion(correo)
            navController.navigate("home") {
                popUpTo("login") { inclusive = true }
            }
        }
    }

    LaunchedEffect(mensajeError) {
        if (mensajeError.isNotEmpty()) {
            mostrarModalError = true
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo_notaria),
            contentDescription = "Logo NotariaX",
            modifier = Modifier.size(160.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "NotariaXPlorer",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.secondary
        )

        Text(
            text = "Iniciar Sesión",
            fontSize = 18.sp,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        OutlinedTextField(
            value = correo,
            onValueChange = { correo = it },
            label = { Text("Correo Electrónico") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = contrasena,
            onValueChange = { contrasena = it },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                if (correo.isNotEmpty() && contrasena.isNotEmpty()) {
                    viewModel.iniciarSesion(correo, contrasena)
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Text("Ingresar")
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = { navController.navigate("registro") }) {
            Text("¿No tienes cuenta? Regístrate aquí")
        }
    }

    if (mostrarModalError) {
        AlertDialog(
            onDismissRequest = {
                mostrarModalError = false
                viewModel.limpiarMensaje()
            },
            title = { Text("Error de autenticación") },
            text = { Text(mensajeError) },
            confirmButton = {
                Button(onClick = {
                    mostrarModalError = false
                    viewModel.limpiarMensaje()
                }) {
                    Text("Aceptar")
                }
            }
        )
    }
}