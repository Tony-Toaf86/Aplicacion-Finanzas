package com.toaf.finance.funcionalidades.transacciones.ui // 👈 Modifica según tu paquete raíz real

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
fun PantallaHistorialTransacciones(
    viewModel: TransaccionesViewModel,
    onVolver: () -> Unit
) {
    val historial by viewModel.historial.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.cargarHistorial()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Historial Global", style = MaterialTheme.typography.headlineMedium)
            Button(onClick = onVolver) { Text("Volver") }
        }

        Spacer(modifier = Modifier.height(16.dp))

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
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "De Cuenta #I${transaccion.cuentaOrigenId} a #D${transaccion.cuentaDestinoId} • $fechaLegible",
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