package com.toaf.finance.funcionalidades.transacciones.ui

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.toaf.finance.datos.modelo.TransaccionEntity
import com.toaf.finance.funcionalidades.transacciones.repositorio.RepositorioTransacciones
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TransaccionesViewModel(private val repositorio: RepositorioTransacciones) : ViewModel() {

    private val _historial = MutableStateFlow<List<TransaccionEntity>>(emptyList())
    val historial: StateFlow<List<TransaccionEntity>> = _historial

    // Estados para el formulario de transferencia
    var cuentaOrigenId = mutableStateOf<Int?>(null)
    var cuentaDestinoId = mutableStateOf<Int?>(null)
    var montoTransferencia = mutableStateOf("")
    var descripcionTransferencia = mutableStateOf("")

    fun cargarHistorial() {
        viewModelScope.launch {
            _historial.value = repositorio.obtenerHistorial()
        }
    }

    fun enviarDinero(onExito: () -> Unit) {
        val origen = cuentaOrigenId.value
        val destino = cuentaDestinoId.value
        val monto = montoTransferencia.value.toDoubleOrNull() ?: 0.0

        if (origen != null && destino != null && monto > 0) {
            viewModelScope.launch {
                repositorio.enviarTransferencia(
                    cuentaOrigenId = origen,
                    cuentaDestinoId = destino,
                    monto = monto,
                    descripcion = descripcionTransferencia.value
                )
                // Limpiamos los campos
                montoTransferencia.value = ""
                descripcionTransferencia.value = ""
                onExito()
            }
        }
    }
}