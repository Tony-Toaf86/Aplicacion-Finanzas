package com.anderson.financeapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.anderson.financeapp.data.AppDatabase
import com.anderson.financeapp.repository.UserRepository
import kotlinx.coroutines.launch

@Composable
fun RechargeScreen(
    userId: Int,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val repository = remember {
        UserRepository(AppDatabase.getDatabase(context).financeDao())
    }

    var amount by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }

    FinanceFormScreen(
        icon = "💰",
        title = "Recargar saldo",
        subtitle = "Agrega dinero simulado a tu cuenta principal",
        message = message,
        buttonText = "RECARGAR",
        onBack = onBack,
        onSubmit = {
            val value = amount.toDoubleOrNull()

            if (value == null) {
                message = "Ingresa un monto válido"
            } else {
                scope.launch {
                    val result = repository.addMoney(
                        userId = userId,
                        title = "Recarga de saldo",
                        amount = value,
                        type = "RECARGA"
                    )

                    if (result.isSuccess) {
                        playSuccessSound(context)
                        onBack()
                    } else {
                        message = result.exceptionOrNull()?.message ?: "Error al recargar"
                    }
                }
            }
        }
    ) {
        OutlinedTextField(
            value = amount,
            onValueChange = { amount = it },
            label = { Text("Monto a recargar") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp)
        )
    }
}