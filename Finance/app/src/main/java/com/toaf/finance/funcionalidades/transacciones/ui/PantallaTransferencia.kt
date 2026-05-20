package com.toaf.finance.funcionalidades.transacciones.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun PantallaTransferencia( // 🛠️ Nombre corregido acorde a tu archivo
    viewModel: TransaccionesViewModel,
    onVolverAlPanel: () -> Unit
) {
    val historial by viewModel.historial.collectAsState()
    val origenId by viewModel.cuentaOrigenId
    val destinoId by viewModel.cuentaDestinoId
    val monto by viewModel.montoTransferencia
    val descripcion by viewModel.descripcionTransferencia

    LaunchedEffect(Unit) {
        viewModel.cargarHistorial()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Transferencias y Movimientos",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "Nueva Transferencia", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = origenId?.toString() ?: "",
                    onValueChange = { viewModel.cuentaOrigenId.value = it.toIntOrNull() },
                    label = { Text("ID Cuenta Origen") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = destinoId?.toString() ?: "",
                    onValueChange = { viewModel.cuentaDestinoId.value = it.toIntOrNull() },
                    label = { Text("ID Cuenta Destino") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = monto,
                    onValueChange = { viewModel.montoTransferencia.value = it },
                    label = { Text("Monto ($)") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = descripcion,
                    onValueChange = { viewModel.descripcionTransferencia.value = it },
                    label = { Text("Descripción") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = {
                        viewModel.enviarDinero {
                            viewModel.cargarHistorial()
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Ejecutar Transferencia")
                }
            }
        }

        Text(
            text = "Historial",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        if (historial.isEmpty()) {
            Text(text = "No se registran movimientos aún.", style = MaterialTheme.typography.bodyMedium)
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(historial) { transaccion ->
                    val formatoFecha = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                    val fechaLegible = formatoFecha.format(Date(transaccion.fecha))

                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(text = transaccion.descripcion, style = MaterialTheme.typography.bodyLarge)
                                Text(
                                    text = "$${transaccion.monto}",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "De Cuenta ${transaccion.cuentaOrigenId} a ${transaccion.cuentaDestinoId} • $fechaLegible",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}