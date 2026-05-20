package com.toaf.finance.funcionalidades.transacciones.repositorio

import com.toaf.finance.datos.modelo.TransaccionEntity

interface RepositorioTransacciones {
    // Contrato para ver el historial de gastos/ingresos
    suspend fun obtenerHistorial(): List<TransaccionEntity>

    // Contrato para enviar dinero de un lado a otro de forma segura
    suspend fun enviarTransferencia(cuentaOrigenId: Int, cuentaDestinoId: Int, monto: Double, descripcion: String)
}