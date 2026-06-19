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
fun ExpenseScreen(userId: Int, onBack: () -> Unit) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val repository = remember { UserRepository(AppDatabase.getDatabase(context).financeDao()) }

    var description by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }

    FinanceFormScreen(
        icon = "📉",
        title = "Registrar gasto",
        subtitle = "Controla tus compras y evita saldo insuficiente",
        message = message,
        buttonText = "GUARDAR GASTO",
        onBack = onBack,
        onSubmit = {
            val value = amount.toDoubleOrNull()

            if (description.isBlank() || value == null) {
                message = "Completa la descripción y un monto válido"
            } else {
                scope.launch {
                    val result = repository.subtractMoney(
                        userId = userId,
                        title = description.trim(),
                        amount = value,
                        type = "GASTO"
                    )

                    if (result.isSuccess) {
                        playSuccessSound(context)
                        onBack()
                    } else {
                        message = result.exceptionOrNull()?.message ?: "Error al guardar gasto"
                    }
                }
            }
        }
    ) {
        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Descripción") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp)
        )

        Spacer(modifier = Modifier.height(14.dp))

        OutlinedTextField(
            value = amount,
            onValueChange = { amount = it },
            label = { Text("Monto") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp)
        )
    }
}