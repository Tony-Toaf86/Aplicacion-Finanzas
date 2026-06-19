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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.anderson.financeapp.data.AppDatabase
import com.anderson.financeapp.repository.UserRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun CreateAccountScreen(
    userId: Int,
    onAccountCreated: () -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val repository = remember {
        UserRepository(AppDatabase.getDatabase(context).financeDao())
    }

    var accountName by remember { mutableStateOf("") }
    var initialBalance by remember { mutableStateOf("") }
    var accountType by remember { mutableStateOf("BANCO") }
    var message by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
            .padding(28.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(42.dp))

        Box(
            modifier = Modifier
                .size(82.dp)
                .background(Color(0xFFEAF4FF), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = when (accountType) {
                    "BANCO" -> "🏦"
                    "TARJETA" -> "💳"
                    else -> "💵"
                },
                fontSize = 34.sp
            )
        }

        Spacer(modifier = Modifier.height(18.dp))

        Text(
            text = "Nueva cuenta",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF0F293D),
            textAlign = TextAlign.Center
        )

        Text(
            text = "Selecciona el tipo de cuenta que deseas crear",
            fontSize = 15.sp,
            color = Color(0xFF526173),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {

                Text(
                    text = "Tipo de cuenta",
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F293D)
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(modifier = Modifier.fillMaxWidth()) {
                    AccountTypeButton(
                        text = "Banco",
                        selected = accountType == "BANCO",
                        onClick = { accountType = "BANCO" },
                        modifier = Modifier.weight(1f)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    AccountTypeButton(
                        text = "Tarjeta",
                        selected = accountType == "TARJETA",
                        onClick = { accountType = "TARJETA" },
                        modifier = Modifier.weight(1f)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    AccountTypeButton(
                        text = "Efectivo",
                        selected = accountType == "EFECTIVO",
                        onClick = { accountType = "EFECTIVO" },
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = accountName,
                    onValueChange = { accountName = it },
                    label = { Text("Nombre de la cuenta") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp)
                )

                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                    value = initialBalance,
                    onValueChange = { initialBalance = it },
                    label = { Text("Saldo inicial") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp)
                )

                if (accountType == "TARJETA") {
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Se generará una tarjeta VISA simulada automáticamente.",
                        fontSize = 13.sp,
                        color = Color(0xFF526173)
                    )
                }

                if (message.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = message,
                        color = if (message.contains("correctamente")) {
                            Color(0xFF16A34A)
                        } else {
                            Color(0xFFEF4444)
                        }
                    )
                }

                Spacer(modifier = Modifier.height(22.dp))

                Button(
                    onClick = {
                        val balance = initialBalance.toDoubleOrNull()

                        if (accountName.isBlank() || balance == null) {
                            message = "Completa el nombre y un saldo válido"
                        } else {
                            scope.launch {
                                val result = repository.createAccount(
                                    userId = userId,
                                    accountName = accountName.trim(),
                                    initialBalance = balance,
                                    accountType = accountType
                                )

                                if (result.isSuccess) {
                                    playSuccessSound(context)
                                    message = "✓ Cuenta creada correctamente"
                                    delay(800)
                                    onAccountCreated()
                                } else {
                                    message = result.exceptionOrNull()?.message ?: "Error al crear cuenta"
                                }
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF2563EB),
                        contentColor = Color.White
                    )
                ) {
                    Text("GUARDAR CUENTA", fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(12.dp))

                TextButton(
                    onClick = onBack,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Volver a mis cuentas", color = Color(0xFF2563EB))
                }
            }
        }
    }
}

@Composable
fun AccountTypeButton(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(44.dp),
        shape = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (selected) Color(0xFF2563EB) else Color(0xFFEAF4FF),
            contentColor = if (selected) Color.White else Color(0xFF2563EB)
        )
    ) {
        Text(
            text = text,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )
    }
}