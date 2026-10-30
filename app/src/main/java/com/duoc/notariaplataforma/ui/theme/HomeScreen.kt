package com.duoc.notariaplataforma.ui

import android.Manifest
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import androidx.navigation.NavController
import com.duoc.notariaplataforma.R
import com.duoc.notariaplataforma.util.SesionManager
import com.duoc.notariaplataforma.viewmodel.UsuarioViewModel
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.io.File
@Composable
fun HomeScreen(navController: NavController, viewModel: UsuarioViewModel) {
    val usuarioLogueado by viewModel.usuarioLogueado.collectAsState()
    val context = LocalContext.current
    val sesionManager = remember { SesionManager(context) }
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var ubicacion by remember { mutableStateOf("Ubicación no obtenida") }
    var mostrarModalUbicacion by remember { mutableStateOf(false) }
    var mostrarModalBienvenida by remember { mutableStateOf(true) }
    var visible by remember { mutableStateOf(false) }

    androidx.compose.runtime.LaunchedEffect(Unit) {
        visible = true
    }

    // Lanzador para tomar foto
    val takePictureLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success ->
        if (success && imageUri != null) {
            val bmp = MediaStore.Images.Media.getBitmap(context.contentResolver, imageUri)
            bitmap = bmp
        }
    }

    // Lanzador de permiso de cámara
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            val file = File.createTempFile("notaria_foto", ".jpg", context.cacheDir)
            file.deleteOnExit()
            val uri = FileProvider.getUriForFile(
                context,
                context.packageName + ".provider",
                file
            )
            imageUri = uri
            takePictureLauncher.launch(uri)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        Image(
            painter = painterResource(id = R.drawable.logo_notaria),
            contentDescription = "Logo NotariaX",
            modifier = Modifier.size(150.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Bienvenido a NotariaXPlorer",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.secondary
        )

        usuarioLogueado?.let {
            Text(
                text = "${it.nombre} ${it.apellido}",
                fontSize = 18.sp,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Servicios Disponibles",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Button(
                    onClick = {
                        cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text("Tomar Foto")
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = { navController.navigate("solicitud") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary)
                ) {
                    Text("Solicitar Trámite Notarial")
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = {
                        viewModel.cerrarSesion()
                        navController.navigate("login") {
                            popUpTo("home") { inclusive = true }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Cerrar Sesión")
                }

                if (bitmap != null) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Documento Fotográfico",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Image(
                        bitmap = bitmap!!.asImageBitmap(),
                        contentDescription = "Foto tomada",
                        modifier = Modifier.size(200.dp)
                    )
                }
            }
            if (mostrarModalBienvenida) {
                AlertDialog(
                    onDismissRequest = { mostrarModalBienvenida = false },
                    title = { Text("¡Bienvenido!") },
                    text = { Text("Has iniciado sesión correctamente en NotariaX.") },
                    confirmButton = {
                        Button(onClick = { mostrarModalBienvenida = false }) {
                            Text("Aceptar")
                        }
                    }
                )
            }
        }
    }
}