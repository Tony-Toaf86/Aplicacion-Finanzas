package com.toaf.finance.datos.base_datos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.toaf.finance.datos.modelo.TransaccionEntity

// clase para manejar las transacciones entre cuentas, incluyendo la lógica de transferencia y el historial de transacciones
@Dao
interface TransaccionDao {

    @Insert
    suspend fun insertarTransaccion(transaccion: TransaccionEntity)

    @Query("SELECT * FROM transacciones ORDER BY fecha DESC")
    suspend fun obtenerHistorialTransacciones(): List<TransaccionEntity>

    @Query("UPDATE cuentas SET saldo = saldo - :monto WHERE id = :cuentaId")
    suspend fun restarSaldo(cuentaId: Int, monto: Double)

    @Query("UPDATE cuentas SET saldo = saldo + :monto WHERE id = :cuentaId")
    suspend fun sumarSaldo(cuentaId: Int, monto: Double)

    @Transaction
    suspend fun realizarTransferencia(cuentaOrigenId: Int, cuentaDestinoId: Int, monto: Double, descripcion: String) {
        restarSaldo(cuentaOrigenId, monto)
        sumarSaldo(cuentaDestinoId, monto)

        val nuevaTransaccion = TransaccionEntity(
            cuentaOrigenId = cuentaOrigenId,
            cuentaDestinoId = cuentaDestinoId,
            monto = monto,
            descripcion = descripcion
        )
        insertarTransaccion(nuevaTransaccion)
    }
}