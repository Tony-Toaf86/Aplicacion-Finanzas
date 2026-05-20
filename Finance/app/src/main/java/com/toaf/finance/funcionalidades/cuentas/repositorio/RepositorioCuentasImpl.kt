package com.toaf.finance.funcionalidades.cuentas.repositorio
import com.toaf.finance.datos.base_datos.CuentaDao
import com.toaf.finance.datos.modelo.CuentaEntity

// Implementación concreta del repositorio de cuentas
class RepositorioCuentasImpl(private val cuentaDao: CuentaDao) : RepositorioCuentas {
    override suspend fun obtenerTodasLasCuentas(): List<CuentaEntity> {
        return cuentaDao.obtenerTodasLasCuentas()
    }

    override suspend fun guardarCuenta(cuenta: CuentaEntity) {
        cuentaDao.insertarCuenta(cuenta)
    }

    // Implementación de actualización de cuenta
    override suspend fun actualizar(cuenta: CuentaEntity) {
        cuentaDao.actualizarCuenta(cuenta)
    }

    // Implementación de eliminación de cuenta
    override suspend fun eliminar(cuenta: CuentaEntity) {
        cuentaDao.eliminarCuenta(cuenta)
    }


}

