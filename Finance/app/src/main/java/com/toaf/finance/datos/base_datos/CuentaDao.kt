package com.toaf.finance.datos.base_datos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.toaf.finance.datos.modelo.CuentaEntity

@Dao
interface CuentaDao {

    @Insert
    suspend fun insertarCuenta(cuenta: CuentaEntity)

    @Query("SELECT * FROM cuentas")
    suspend fun obtenerTodasLasCuentas(): List<CuentaEntity>

    @Update
    suspend fun actualizarCuenta(cuenta: CuentaEntity)

    @Query("SELECT * FROM cuentas WHERE id = :id")
    suspend fun obtenerCuentaPorId(id: Int): CuentaEntity?
}