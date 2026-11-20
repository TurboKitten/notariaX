package com.duoc.notariaplataforma.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.duoc.notariaplataforma.model.Solicitud
import com.duoc.notariaplataforma.viewmodel.SolicitudViewModel

@Composable
fun MisSolicitudesScreen(navController: NavController, viewModel: SolicitudViewModel) {
    val solicitudes by viewModel.solicitudes.collectAsState()
    val cargando by viewModel.cargandoSolicitudes.collectAsState()
    val error by viewModel.errorSolicitudes.collectAsState()
    val mensajeOperacion by viewModel.mensajeOperacionSolicitud.collectAsState()

    var solicitudAEditar by remember { mutableStateOf<Solicitud?>(null) }
    var solicitudAEliminar by remember { mutableStateOf<Solicitud?>(null) }
    var mostrarMensajeOperacion by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.cargarSolicitudes()
    }

    LaunchedEffect(mensajeOperacion) {
        if (mensajeOperacion.isNotBlank()) {
            mostrarMensajeOperacion = true
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Mis Solicitudes",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = { viewModel.cargarSolicitudes() },
                modifier = Modifier.weight(1f)
            ) {
                Text("Actualizar")
            }
            Button(
                onClick = { navController.navigate("solicitud") },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
            ) {
                Text("Nueva solicitud")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        when {
            cargando -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator()
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Cargando solicitudes...")
                }
            }

            error.isNotBlank() -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(error, color = MaterialTheme.colorScheme.error)
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(onClick = { viewModel.cargarSolicitudes() }) {
                        Text("Reintentar")
                    }
                }
            }

            solicitudes.isEmpty() -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Aún no has realizado ninguna solicitud")
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = { navController.navigate("solicitud") }) {
                        Text("Crear solicitud")
                    }
                }
            }

            else -> {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(solicitudes, key = { it.id }) { solicitud ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text("ID: ${solicitud.id} - ${solicitud.tipoTramite}", fontWeight = FontWeight.Bold)
                                Text("Solicitante: ${solicitud.nombreSolicitante}")
                                Text("Prioridad: ${solicitud.prioridad}")
                                Text("Copias: ${solicitud.cantidadCopias}")
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Estado: ${solicitud.estado}",
                                    color = MaterialTheme.colorScheme.tertiary,
                                    fontWeight = FontWeight.Bold
                                )

                                Spacer(modifier = Modifier.height(12.dp))
                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Button(
                                        onClick = { solicitudAEditar = solicitud },
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text("Editar")
                                    }
                                    Button(
                                        onClick = { solicitudAEliminar = solicitud },
                                        modifier = Modifier.weight(1f),
                                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                                    ) {
                                        Text("Eliminar")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = { navController.navigate("home") },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Text("Volver al Inicio")
        }
    }

    solicitudAEditar?.let { solicitud ->
        EditarSolicitudDialog(
            solicitud = solicitud,
            onDismiss = { solicitudAEditar = null },
            onConfirm = { estado, prioridad, copias ->
                viewModel.actualizarSolicitud(
                    solicitudId = solicitud.id,
                    nombreSolicitante = solicitud.nombreSolicitante,
                    tipoTramite = solicitud.tipoTramite,
                    prioridad = prioridad,
                    cantidadCopias = copias,
                    estado = estado
                )
                solicitudAEditar = null
            }
        )
    }

    solicitudAEliminar?.let { solicitud ->
        AlertDialog(
            onDismissRequest = { solicitudAEliminar = null },
            title = { Text("Eliminar solicitud") },
            text = { Text("¿Deseas eliminar la solicitud #${solicitud.id}? Esta acción no se puede deshacer.") },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.eliminarSolicitud(solicitud.id)
                        solicitudAEliminar = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                TextButton(onClick = { solicitudAEliminar = null }) {
                    Text("Cancelar")
                }
            }
        )
    }

    if (mostrarMensajeOperacion && mensajeOperacion.isNotBlank()) {
        AlertDialog(
            onDismissRequest = {
                mostrarMensajeOperacion = false
                viewModel.limpiarEstadoOperacionSolicitud()
            },
            title = { Text("Operación") },
            text = { Text(mensajeOperacion) },
            confirmButton = {
                Button(onClick = {
                    mostrarMensajeOperacion = false
                    viewModel.limpiarEstadoOperacionSolicitud()
                }) {
                    Text("Aceptar")
                }
            }
        )
    }
}

@Composable
private fun EditarSolicitudDialog(
    solicitud: Solicitud,
    onDismiss: () -> Unit,
    onConfirm: (estado: String, prioridad: String, copias: Int) -> Unit
) {
    var estado by remember { mutableStateOf(solicitud.estado) }
    var prioridad by remember { mutableStateOf(solicitud.prioridad) }
    var copiasTexto by remember { mutableStateOf(solicitud.cantidadCopias.toString()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Editar solicitud #${solicitud.id}") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = estado,
                    onValueChange = { estado = it },
                    label = { Text("Estado") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = prioridad,
                    onValueChange = { prioridad = it },
                    label = { Text("Prioridad") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = copiasTexto,
                    onValueChange = { copiasTexto = it.filter { ch -> ch.isDigit() }.take(1) },
                    label = { Text("Cantidad de copias (1-5)") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                val copias = copiasTexto.toIntOrNull()?.coerceIn(1, 5) ?: solicitud.cantidadCopias
                onConfirm(estado.ifBlank { solicitud.estado }, prioridad.ifBlank { solicitud.prioridad }, copias)
            }) {
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}
