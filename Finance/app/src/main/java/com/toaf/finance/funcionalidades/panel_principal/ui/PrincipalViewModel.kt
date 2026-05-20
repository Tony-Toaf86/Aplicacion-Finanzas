package com.toaf.finance.funcionalidades.panel_principal.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.toaf.finance.funcionalidades.cuentas.repositorio.RepositorioCuentas
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PrincipalViewModel(private val repositorioCuentas: RepositorioCuentas) : ViewModel() {

    private val _saldoTotal = MutableStateFlow(0.0)
    val saldoTotal: StateFlow<Double> = _saldoTotal

    // Suma el saldo de todas las cuentas existentes para dar el balance global
    fun calcularSaldoGlobal() {
        viewModelScope.launch {
            val cuentas = repositorioCuentas.obtenerTodasLasCuentas()
            _saldoTotal.value = cuentas.sumOf { it.saldo }
        }
    }
}