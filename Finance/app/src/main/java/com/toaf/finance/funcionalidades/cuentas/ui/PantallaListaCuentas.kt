package com.toaf.finance.funcionalidades.cuentas.ui

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
import com.toaf.finance.datos.modelo.CuentaEntity

@Composable
fun PantallaListaCuentas(viewModel: CuentasViewModel) {
    val cuentas by viewModel.cuentas.collectAsState()
    val nombre by viewModel.nombreCuenta
    val saldo by viewModel.saldoInicial

    // Cargamos las cuentas existentes al entrar a la pantalla
    LaunchedEffect(Unit) {
        viewModel.cargarCuentas()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Mis Cuentas",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // --- FORMULARIO PARA CREAR CUENTA ---
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "Nueva Cuenta", style = MaterialTheme.typography.titleMedium)

                OutlinedTextField(
                    value = nombre,
                    onValueChange = { viewModel.nombreCuenta.value = it },
                    label = { Text("Nombre (ej: Banco)") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = saldo,
                    onValueChange = { viewModel.saldoInicial.value = it },
                    label = { Text("Saldo Inicial ($)") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = {
                        viewModel.crearCuenta {
                            // Cuando se guarde con éxito en Room, recargamos la lista
                            viewModel.cargarCuentas()
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Guardar Cuenta")
                }
            }
        }

        // --- LISTA DE CUENTAS EN LA BASE DE DATOS ---
        Text(
            text = "Cuentas Activas",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        if (cuentas.isEmpty()) {
            Text(text = "No hay cuentas creadas todavía.", style = MaterialTheme.typography.bodyMedium)
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(cuentas) { cuenta ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = cuenta.nombre, style = MaterialTheme.typography.bodyLarge)
                            Text(
                                text = "$${String.format("%.2f", cuenta.saldo)}",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
        }
    }
}