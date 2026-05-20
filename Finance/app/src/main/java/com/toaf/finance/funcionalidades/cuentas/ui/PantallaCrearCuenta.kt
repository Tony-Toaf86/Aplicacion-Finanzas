package com.toaf.finance.funcionalidades.cuentas.ui // 👈 Modifica según tu paquete raíz real

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PantallaCrearCuenta(
    viewModel: CuentasViewModel,
    onCuentaCreada: () -> Unit,
    onVolver: () -> Unit
) {
    val nombre by viewModel.nombreCuenta
    val saldo by viewModel.saldoInicial

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Crear Nueva Cuenta",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        OutlinedTextField(
            value = nombre,
            onValueChange = { viewModel.nombreCuenta.value = it },
            label = { Text("Nombre (ej: Tarjeta, Efectivo)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = saldo,
            onValueChange = { viewModel.saldoInicial.value = it },
            label = { Text("Saldo Inicial ($)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                viewModel.crearCuenta {
                    onCuentaCreada()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Guardar Cuenta")
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextButton(
            onClick = onVolver,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Cancelar")
        }
    }
}