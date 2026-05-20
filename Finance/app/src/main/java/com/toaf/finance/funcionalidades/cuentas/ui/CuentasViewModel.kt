package com.toaf.finance.funcionalidades.cuentas.ui

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.toaf.finance.datos.modelo.CuentaEntity
import com.toaf.finance.funcionalidades.cuentas.repositorio.RepositorioCuentas
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CuentasViewModel(private val repositorio: RepositorioCuentas) : ViewModel() {

    // Estado que guarda la lista de cuentas para la pantalla
    private val _cuentas = MutableStateFlow<List<CuentaEntity>>(emptyList())
    val cuentas: StateFlow<List<CuentaEntity>> = _cuentas

    // Estados temporales para los formularios de creación
    var nombreCuenta = mutableStateOf("")
    var saldoInicial = mutableStateOf("")

    // Carga las cuentas desde la base de datos
    fun cargarCuentas() {
        viewModelScope.launch {
            _cuentas.value = repositorio.obtenerTodasLasCuentas()
        }
    }

    // Guarda una nueva cuenta usando los datos del formulario
    fun crearCuenta(onExito: () -> Unit) {
        val saldo = saldoInicial.value.toDoubleOrNull() ?: 0.0
        if (nombreCuenta.value.isNotBlank()) {
            viewModelScope.launch {
                val nuevaCuenta = CuentaEntity(nombre = nombreCuenta.value, saldo = saldo)
                repositorio.guardarCuenta(nuevaCuenta)

                // Limpiamos el formulario y avisamos que se guardó con éxito
                nombreCuenta.value = ""
                saldoInicial.value = ""
                onExito()
            }
        }
    }
}