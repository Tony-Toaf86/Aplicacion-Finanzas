package com.anderson.financeapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.anderson.financeapp.data.AccountEntity
import com.anderson.financeapp.data.AppDatabase
import com.anderson.financeapp.repository.UserRepository
import kotlinx.coroutines.launch

@Composable
fun TransferScreen(userId: Int, onBack: () -> Unit) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val repository = remember { UserRepository(AppDatabase.getDatabase(context).financeDao()) }

    var senderAccounts by remember { mutableStateOf<List<AccountEntity>>(emptyList()) }
    var receiverAccounts by remember { mutableStateOf<List<AccountEntity>>(emptyList()) }

    var selectedSenderAccount by remember { mutableStateOf<AccountEntity?>(null) }
    var selectedReceiverAccount by remember { mutableStateOf<AccountEntity?>(null) }

    var receiverEmail by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }

    LaunchedEffect(userId) {
        senderAccounts = repository.getAccountsForUser(userId)
        selectedSenderAccount = senderAccounts.firstOrNull()
    }

    Column(
        Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
            .padding(24.dp)
    ) {
        Text(
            text = "Transferir dinero",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF0F293D)
        )

        Text(
            text = "Envía dinero a otro usuario registrado",
            fontSize = 15.sp,
            color = Color(0xFF526173)
        )

        Spacer(Modifier.height(18.dp))

        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            item {
                Text("Cuenta origen", fontWeight = FontWeight.Bold)

                Spacer(Modifier.height(8.dp))

                senderAccounts.forEach { account ->
                    SelectableAccountCard(
                        account = account,
                        selected = selectedSenderAccount?.id == account.id,
                        onClick = { selectedSenderAccount = account }
                    )
                }

                Spacer(Modifier.height(16.dp))

                OutlinedTextField(
                    value = receiverEmail,
                    onValueChange = {
                        receiverEmail = it
                        receiverAccounts = emptyList()
                        selectedReceiverAccount = null
                    },
                    label = { Text("Correo del destinatario") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp)
                )

                Spacer(Modifier.height(10.dp))

                Button(
                    onClick = {
                        if (receiverEmail.isBlank()) {
                            message = "Ingresa el correo del destinatario"
                        } else {
                            scope.launch {
                                val receiver = repository.findUserByEmail(receiverEmail.trim())

                                if (receiver == null) {
                                    message = "No existe un usuario con ese correo"
                                    receiverAccounts = emptyList()
                                    selectedReceiverAccount = null
                                } else if (receiver.id == userId) {
                                    message = "No puedes transferirte a ti mismo"
                                    receiverAccounts = emptyList()
                                    selectedReceiverAccount = null
                                } else {
                                    receiverAccounts = repository.getAccountsForUser(receiver.id)
                                    selectedReceiverAccount = receiverAccounts.firstOrNull()
                                    message = "Usuario encontrado. Selecciona cuenta destino."
                                }
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2563EB))
                ) {
                    Text("BUSCAR DESTINATARIO")
                }

                if (receiverAccounts.isNotEmpty()) {
                    Spacer(Modifier.height(16.dp))

                    Text("Cuenta destino", fontWeight = FontWeight.Bold)

                    Spacer(Modifier.height(8.dp))

                    receiverAccounts.forEach { account ->
                        SelectableAccountCard(
                            account = account,
                            selected = selectedReceiverAccount?.id == account.id,
                            onClick = { selectedReceiverAccount = account }
                        )
                    }
                }

                Spacer(Modifier.height(16.dp))

                OutlinedTextField(
                    value = amount,
                    onValueChange = { amount = it },
                    label = { Text("Monto") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp)
                )

                if (message.isNotEmpty()) {
                    Spacer(Modifier.height(12.dp))
                    Text(
                        text = message,
                        color = if (message.contains("encontrado") || message.contains("correctamente")) {
                            Color(0xFF16A34A)
                        } else {
                            Color(0xFFEF4444)
                        }
                    )
                }

                Spacer(Modifier.height(20.dp))

                Button(
                    onClick = {
                        val value = amount.toDoubleOrNull()
                        val origin = selectedSenderAccount
                        val destination = selectedReceiverAccount

                        when {
                            origin == null -> message = "Selecciona una cuenta origen"
                            destination == null -> message = "Busca y selecciona una cuenta destino"
                            value == null -> message = "Ingresa un monto válido"
                            else -> {
                                scope.launch {
                                    val result = repository.transferBetweenUsers(
                                        senderUserId = userId,
                                        senderAccountId = origin.id,
                                        receiverEmail = receiverEmail.trim(),
                                        receiverAccountId = destination.id,
                                        amount = value
                                    )

                                    if (result.isSuccess) {
                                        playSuccessSound(context)
                                        message = "Transferencia realizada correctamente"
                                        onBack()
                                    } else {
                                        message = result.exceptionOrNull()?.message ?: "Error al transferir"
                                    }
                                }
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2563EB))
                ) {
                    Text("TRANSFERIR")
                }

                Spacer(Modifier.height(12.dp))

                OutlinedButton(
                    onClick = onBack,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text("VOLVER")
                }
            }
        }
    }
}

@Composable
fun SelectableAccountCard(
    account: AccountEntity,
    selected: Boolean,
    onClick: () -> Unit
) {
    val icon = when (account.accountType) {
        "BANCO" -> "🏦"
        "TARJETA" -> "💳"
        "EFECTIVO" -> "💵"
        else -> "💼"
    }

    val subtitle = when (account.accountType) {
        "BANCO" -> "Cuenta de ahorro"
        "TARJETA" -> "VISA **** ${account.cardNumber.takeLast(4)}"
        "EFECTIVO" -> "Dinero en efectivo"
        else -> "Cuenta financiera"
    }

    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (selected) Color(0xFFEAF4FF) else Color.White
        )
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text("$icon ${account.accountName}", fontWeight = FontWeight.Bold)
                Text(subtitle, fontSize = 13.sp, color = Color(0xFF526173))
            }

            Text(
                text = "L ${String.format("%.2f", account.balance)}",
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2563EB)
            )
        }
    }
}