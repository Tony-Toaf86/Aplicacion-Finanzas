package com.toaf.finance.funcionalidades.cuentas.repositorio

import com.toaf.finance.datos.modelo.CuentaEntity

interface RepositorioCuentas {
    // Contrato para obtener todas las cuentas en tiempo real
    suspend fun obtenerTodasLasCuentas(): List<CuentaEntity>

    // Contrato para crear una nueva cuenta
    suspend fun guardarCuenta(cuenta: CuentaEntity)
}
