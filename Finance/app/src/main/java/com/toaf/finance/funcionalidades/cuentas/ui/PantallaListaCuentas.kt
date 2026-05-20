package com.toaf.finance.funcionalidades.cuentas.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.toaf.finance.datos.modelo.CuentaEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaListaCuentas(
    viewModel: CuentasViewModel,
    onIrACrearCuenta: () -> Unit,
    onVolver: () -> Unit
) {
    val cuentas by viewModel.cuentas.collectAsState()

    // Estados locales para controlar el flujo de Edición (Update)
    var cuentaAEditar by remember { mutableStateOf<CuentaEntity?>(null) }
    var nombreEditado by remember { mutableStateOf("") }
    var saldoEditado by remember { mutableStateOf("") }

    // Cada vez que entramos, refrescamos los datos de Room
    LaunchedEffect(Unit) {
        viewModel.cargarCuentas()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Gestión de Cuentas") },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        },
        floatingActionButton = {
            // [C]REAR: Botón flotante para ir al formulario de nueva cuenta
            FloatingActionButton(onClick = onIrACrearCuenta) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Añadir Cuenta")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Text(
                text = "Tus Monederos",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            if (cuentas.isEmpty()) {
                Text(
                    text = "No hay cuentas disponibles. Presiona el botón '+' abajo.",
                    style = MaterialTheme.typography.bodyMedium
                )
            } else {
                // [R]EAD: Lista que lee el estado de Room
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(cuentas) { cuenta ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    // Al hacer clic en la tarjeta, abrimos el modo edición
                                    cuentaAEditar = cuenta
                                    nombreEditado = cuenta.nombre
                                    saldoEditado = cuenta.saldo.toString()
                                }
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(text = cuenta.nombre, style = MaterialTheme.typography.bodyLarge)
                                    Text(
                                        text = "$${String.format("%.2f", cuenta.saldo)}",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }

                                // Fila de acciones CRUD para la cuenta
                                Row {
                                    IconButton(onClick = {
                                        cuentaAEditar = cuenta
                                        nombreEditado = cuenta.nombre
                                        saldoEditado = cuenta.saldo.toString()
                                    }) {
                                        Icon(
                                            imageVector = Icons.Default.Edit,
                                            contentDescription = "Editar cuenta",
                                            tint = MaterialTheme.colorScheme.outline
                                        )
                                    }

                                    // [D]ELETE: Acción de borrado directo
                                    IconButton(onClick = {
                                        // Añade una función eliminar en tu ViewModel si no la tienes, o usa esta lógica directa:
                                        viewModel.eliminarCuenta(cuenta) {
                                            viewModel.cargarCuentas() // Recargamos la lista
                                        }
                                    }) {
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = "Eliminar cuenta",
                                            tint = MaterialTheme.colorScheme.error
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // --- [U]PDATE: DIÁLOGO EMERGENTE PARA EDITAR LA CUENTA ---
        cuentaAEditar?.let { cuenta ->
            AlertDialog(
                onDismissRequest = { cuentaAEditar = null },
                title = { Text("Editar Cuenta") },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = nombreEditado,
                            onValueChange = { nombreEditado = it },
                            label = { Text("Nombre de la cuenta") }
                        )
                        OutlinedTextField(
                            value = saldoEditado,
                            onValueChange = { saldoEditado = it },
                            label = { Text("Saldo actual ($)") }
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            val saldoDouble = saldoEditado.toDoubleOrNull() ?: cuenta.saldo
                            // Creamos el objeto modificado manteniendo el mismo ID para que Room reemplace
                            val cuentaActualizada = cuenta.copy(nombre = nombreEditado, saldo = saldoDouble)

                            viewModel.actualizarCuenta(cuentaActualizada) {
                                cuentaAEditar = null
                                viewModel.cargarCuentas() // Refrescamos la UI
                            }
                        }
                    ) {
                        Text("Actualizar")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { cuentaAEditar = null }) {
                        Text("Cancelar")
                    }
                }
            )
        }
    }
}