package com.duoc.notariaplataforma.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.duoc.notariaplataforma.model.Solicitud
import com.duoc.notariaplataforma.viewmodel.UsuarioViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SolicitudTramiteScreen(navController: NavController, viewModel: UsuarioViewModel) {
    var nombreSolicitante by remember { mutableStateOf("") }
    var tipoTramite by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var urgencia by remember { mutableStateOf("Normal") }
    var aceptaTerminos by remember { mutableStateOf(false) }
    var cantidadCopias by remember { mutableFloatStateOf(1f) }
    var mostrarModal by remember { mutableStateOf(false) }

    val tiposTramite = listOf("Escritura Pública", "Poder Notarial", "Certificación", "Autenticación", "Protesto")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Solicitud de Trámite Notarial",
            fontSize = 22.sp,
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        OutlinedTextField(
            value = nombreSolicitante,
            onValueChange = { nombreSolicitante = it },
            label = { Text("Nombre del Solicitante") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = tipoTramite,
                onValueChange = {},
                readOnly = true,
                label = { Text("Tipo de Trámite") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier.menuAnchor().fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                tiposTramite.forEach { tipo ->
                    DropdownMenuItem(
                        text = { Text(tipo) },
                        onClick = {
                            tipoTramite = tipo
                            expanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Prioridad del Trámite",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.align(Alignment.Start).padding(bottom = 8.dp)
        )

        Column(modifier = Modifier.align(Alignment.Start)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(selected = urgencia == "Normal", onClick = { urgencia = "Normal" })
                Text("Normal")
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(selected = urgencia == "Urgente", onClick = { urgencia = "Urgente" })
                Text("Urgente")
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(selected = urgencia == "Muy Urgente", onClick = { urgencia = "Muy Urgente" })
                Text("Muy Urgente")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Cantidad de Copias: ${cantidadCopias.toInt()}",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.align(Alignment.Start)
        )

        Slider(
            value = cantidadCopias,
            onValueChange = { cantidadCopias = it },
            valueRange = 1f..5f,
            steps = 3,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.align(Alignment.Start)) {
            Checkbox(checked = aceptaTerminos, onCheckedChange = { aceptaTerminos = it })
            Text("Acepto los términos y condiciones")
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (nombreSolicitante.isNotEmpty() && tipoTramite.isNotEmpty() && aceptaTerminos) {
                    val solicitud = Solicitud(
                        nombreSolicitante = nombreSolicitante,
                        tipoTramite = tipoTramite,
                        prioridad = urgencia,
                        cantidadCopias = cantidadCopias.toInt(),
                        estado = "En Proceso"
                    )
                    viewModel.guardarSolicitud(solicitud)
                    mostrarModal = true
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = nombreSolicitante.isNotEmpty() && tipoTramite.isNotEmpty() && aceptaTerminos,
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Text("Enviar Solicitud")
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = { navController.navigate("home") },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
        ) {
            Text("Volver al Inicio")
        }
    }

    if (mostrarModal) {
        AlertDialog(
            onDismissRequest = { mostrarModal = false },
            title = { Text("Solicitud Enviada") },
            text = { Text("Tu solicitud ha sido registrada correctamente.\n\nEstado: En Proceso") },
            confirmButton = {
                Button(onClick = {
                    mostrarModal = false
                    navController.navigate("misSolicitudes")
                }) {
                    Text("Ver mis solicitudes")
                }
            },
            dismissButton = {
                Button(onClick = {
                    mostrarModal = false
                    navController.navigate("home")
                }) {
                    Text("Volver al inicio")
                }
            }
        )
    }
}