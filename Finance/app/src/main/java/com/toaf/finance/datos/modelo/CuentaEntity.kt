package com.toaf.finance.datos.modelo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cuentas")
data class CuentaEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0, // Identificador único para cada cuenta
    val nombre: String,                              // Ejemplo: "Efectivo", "Tarjeta de Débito"
    val saldo: Double,                               // El dinero disponible actual (Ej: 2500.50)
    val tipoMoneda: String = "USD"                   // Por si quieres manejar divisas (Ej: "USD", "MXN", "EUR")
)