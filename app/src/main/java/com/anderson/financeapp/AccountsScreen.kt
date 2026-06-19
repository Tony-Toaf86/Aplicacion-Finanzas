package com.anderson.financeapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.anderson.financeapp.data.AccountEntity
import com.anderson.financeapp.data.AppDatabase
import com.anderson.financeapp.repository.UserRepository
import kotlinx.coroutines.launch

@Composable
fun AccountsScreen(
    userId: Int,
    onCreateAccount: () -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val repository = remember {
        UserRepository(AppDatabase.getDatabase(context).financeDao())
    }

    var accounts by remember { mutableStateOf<List<AccountEntity>>(emptyList()) }

    LaunchedEffect(userId) {
        scope.launch {
            accounts = repository.getUserAccounts(userId)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
            .padding(24.dp)
    ) {
        Text(
            text = "Mis cuentas",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF0F293D)
        )

        Text(
            text = "Gestiona tus cuentas financieras",
            fontSize = 15.sp,
            color = Color(0xFF526173)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = onCreateAccount,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF2563EB),
                contentColor = Color.White
            )
        ) {
            Text("CREAR NUEVA CUENTA")
        }

        Spacer(modifier = Modifier.height(18.dp))

        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            items(accounts) { account ->
                AccountCard(account = account)
            }
        }

        OutlinedButton(
            onClick = onBack,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text("VOLVER")
        }
    }
}

@Composable
fun AccountCard(account: AccountEntity) {
    val icon = when (account.accountType) {
        "BANCO" -> "🏦"
        "TARJETA" -> "💳"
        "EFECTIVO" -> "💵"
        else -> "💼"
    }

    val subtitle = when (account.accountType) {
        "BANCO" -> "Cuenta de ahorro"
        "TARJETA" -> {
            val last4 = if (account.cardNumber.length >= 4) {
                account.cardNumber.takeLast(4)
            } else {
                "0000"
            }
            "VISA **** $last4"
        }
        "EFECTIVO" -> "Dinero en efectivo"
        else -> "Cuenta financiera"
    }

    val circleColor = when (account.accountType) {
        "BANCO" -> Color(0xFFEAF4FF)
        "TARJETA" -> Color(0xFFEDE9FE)
        "EFECTIVO" -> Color(0xFFDCFCE7)
        else -> Color(0xFFE5E7EB)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 7.dp),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(54.dp)
                    .background(circleColor, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = icon,
                    fontSize = 26.sp
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = account.accountName,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F293D)
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = subtitle,
                    fontSize = 14.sp,
                    color = Color(0xFF526173)
                )
            }

            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = "Saldo",
                    fontSize = 12.sp,
                    color = Color(0xFF526173)
                )

                Text(
                    text = "L ${String.format("%.2f", account.balance)}",
                    fontSize = 16.sp,
                    color = Color(0xFF2563EB),
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}