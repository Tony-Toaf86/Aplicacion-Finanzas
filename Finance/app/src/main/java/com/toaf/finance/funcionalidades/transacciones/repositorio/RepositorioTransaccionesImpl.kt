package com.toaf.finance.funcionalidades.transacciones.repositorio


import com.toaf.finance.datos.base_datos.TransaccionDao
import com.toaf.finance.datos.modelo.TransaccionEntity


// Implementación concreta del repositorio de transacciones
class RepositorioTransaccionesImpl(private val transaccionDao: TransaccionDao) : RepositorioTransacciones {
    override suspend fun obtenerHistorial(): List<TransaccionEntity> {
        return transaccionDao.obtenerHistorialTransacciones()
    }

    override suspend fun enviarTransferencia(cuentaOrigenId: Int, cuentaDestinoId: Int, monto: Double, descripcion: String) {
        transaccionDao.realizarTransferencia(cuentaOrigenId, cuentaDestinoId, monto, descripcion)
    }
}