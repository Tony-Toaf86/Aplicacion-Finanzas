package com.toaf.finance

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.toaf.finance.datos.base_datos.BaseDatosApp
import com.toaf.finance.funcionalidades.cuentas.repositorio.RepositorioCuentasImpl
import com.toaf.finance.funcionalidades.cuentas.ui.CuentasViewModel
import com.toaf.finance.funcionalidades.cuentas.ui.PantallaCrearCuenta
import com.toaf.finance.funcionalidades.cuentas.ui.PantallaListaCuentas
import com.toaf.finance.funcionalidades.panel_principal.ui.PantallaPrincipal
import com.toaf.finance.funcionalidades.panel_principal.ui.PrincipalViewModel
import com.toaf.finance.funcionalidades.transacciones.repositorio.RepositorioTransaccionesImpl
import com.toaf.finance.funcionalidades.transacciones.ui.PantallaHistorialTransacciones
import com.toaf.finance.funcionalidades.transacciones.ui.PantallaTransferencia
import com.toaf.finance.funcionalidades.transacciones.ui.TransaccionesViewModel
import com.toaf.finance.ui.theme.FinanceTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Mantiene el diseño extendido hacia las barras de estado
        enableEdgeToEdge()
//        ccomentario
        setContent {
            FinanceTheme {
                // 1. Inicializamos Room y sus DAOs de forma segura reteniendo la instancia
                val baseDatos = remember { BaseDatosApp.obtenerBaseDatos(applicationContext) }
                val cuentaDao = remember { baseDatos.cuentaDao() }
                val transaccionDao = remember { baseDatos.transaccionDao() }


                // 2. Acoplamos las implementaciones de los Repositorios
                val repositorioCuentas = remember { RepositorioCuentasImpl(cuentaDao) }
                val repositorioTransacciones = remember { RepositorioTransaccionesImpl(transaccionDao) }

                // 3. Creamos las instancias de los ViewModels pasando sus dependencias
                val principalViewModel = remember { PrincipalViewModel(repositorioCuentas) }
                val cuentasViewModel = remember { CuentasViewModel(repositorioCuentas) }
                val transaccionesViewModel = remember { TransaccionesViewModel(repositorioTransacciones) }

                // 4. Estado de navegación localizado ("principal", "lista_cuentas", "crear_cuenta", "transferencia", "historial")
                var pantallaActual by remember { mutableStateOf("principal") }

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    // Agregamos un contenedor básico modificado por las barras del sistema (Padding)
                    androidx.compose.foundation.layout.Box(modifier = Modifier.padding(innerPadding)) {
                        when (pantallaActual) {
                            "principal" -> {
                                PantallaPrincipal(
                                    viewModel = principalViewModel,
                                    onIrACuentas = { pantallaActual = "lista_cuentas" }
                                )
                            }

                            "lista_cuentas" -> {
                                PantallaListaCuentas(
                                    viewModel = cuentasViewModel,
                                    onIrACrearCuenta = { pantallaActual = "crear_cuenta" },
                                    onVolver = { pantallaActual = "principal" }
                                )
                            }

                            "crear_cuenta" -> {
                                PantallaCrearCuenta(
                                    viewModel = cuentasViewModel,
                                    onCuentaCreada = { pantallaActual = "lista_cuentas" },
                                    onVolver = { pantallaActual = "lista_cuentas" }
                                )
                            }

                            "transferencia" -> {
                                PantallaTransferencia(
                                    viewModel = transaccionesViewModel,
                                    onVolverAlPanel = { pantallaActual = "principal" }
                                )
                            }

                            "historial" -> {
                                PantallaHistorialTransacciones(
                                    viewModel = transaccionesViewModel,
                                    onVolver = { pantallaActual = "principal" }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}