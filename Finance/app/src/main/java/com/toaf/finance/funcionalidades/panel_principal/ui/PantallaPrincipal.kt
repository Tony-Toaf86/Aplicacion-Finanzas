package com.toaf.finance.funcionalidades.panel_principal.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PantallaPrincipal(
    viewModel: PrincipalViewModel,
    onIrACuentas: () -> Unit
) {
    // Escuchamos el estado del saldo global en tiempo real
    val saldoTotal by viewModel.saldoTotal.collectAsState()

    // Cada vez que entramos a esta pantalla, recalculamos el saldo
    LaunchedEffect(Unit) {
        viewModel.calcularSaldoGlobal()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Mi Gestor de Finanzas",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Balance Total Global", style = MaterialTheme.typography.bodyLarge)
                Spacer(modifier = Modifier.height(8.dp))
                // Mostramos el saldo acumulado de la base de datos
                Text(
                    text = "$${String.format("%.2f", saldoTotal)}",
                    style = MaterialTheme.typography.displayMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        Button(
            onClick = onIrACuentas,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "VER MIS CUENTAS")
        }
    }
}