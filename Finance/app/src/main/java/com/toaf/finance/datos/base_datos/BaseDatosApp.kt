package com.toaf.finance.datos.base_datos

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.toaf.finance.datos.modelo.CuentaEntity
import com.toaf.finance.datos.modelo.TransaccionEntity

// 1. Le decimos a Room qué entidades (tablas) componen la base de datos y su versión.
@Database(entities = [CuentaEntity::class, TransaccionEntity::class], version = 1, exportSchema = false)
abstract class BaseDatosApp : RoomDatabase() {

    // 2. Conectamos los DAOs para que la app pueda acceder a ellos a través de esta clase.
    abstract fun cuentaDao(): CuentaDao
    abstract fun transaccionDao(): TransaccionDao

        companion object {
        // @Volatile asegura que cualquier cambio en la base de datos sea visible para todos los hilos de ejecución de inmediato.
        @Volatile
        private var INSTANCE: BaseDatosApp? = null

        // 3. Esta función crea o devuelve la base de datos si ya existe.
        fun obtenerBaseDatos(context: Context): BaseDatosApp {
            // Si ya existe una instancia, la devuelve directamente.
            return INSTANCE ?: synchronized(this) {
                // Si no existe, construye la base de datos SQLite con el nombre "finanzas_db"
                val instancia = Room.databaseBuilder(
                    context.applicationContext,
                    BaseDatosApp::class.java,
                    "finanzas_db"
                )
                    // Esto permite que Room limpie y rehaga las tablas de forma segura si cambias la estructura en el futuro.
                    .fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instancia
                instancia
            }
        }
    }
}