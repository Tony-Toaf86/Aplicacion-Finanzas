package com.toaf.finance.datos.modelo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transacciones")
data class TransaccionEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0, // Identificador único de la transacción
    val cuentaOrigenId: Int,                         // El ID de la cuenta de donde sale el dinero
    val cuentaDestinoId: Int,                        // El ID de la cuenta a donde entra el dinero
    val monto: Double,                               // La cantidad de dinero movida
    val descripcion: String,                         // El concepto (Ej: "Pago de luz", "Transferencia de ahorros")
    val fecha: Long = System.currentTimeMillis()     // La fecha exacta guardada en milisegundos
)
