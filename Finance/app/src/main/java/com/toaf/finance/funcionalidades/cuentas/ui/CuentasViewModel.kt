package com.toaf.finance.funcionalidades.cuentas.ui

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.toaf.finance.datos.modelo.CuentaEntity
import com.toaf.finance.funcionalidades.cuentas.repositorio.RepositorioCuentas
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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

    fun actualizarCuenta(cuenta: CuentaEntity, onCompleto: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            repositorio.actualizar(cuenta)
            withContext(Dispatchers.Main) {
                onCompleto()
            }
        }
    }

    fun eliminarCuenta(cuenta: CuentaEntity, onCompleto: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            repositorio.eliminar(cuenta) // Asegúrate de tener 'eliminar' en tu interfaz de repositorio
            withContext(Dispatchers.Main) {
                onCompleto()
            }
        }
    }
}