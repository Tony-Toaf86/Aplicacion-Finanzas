package com.anderson.financeapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.anderson.financeapp.data.AppDatabase
import com.anderson.financeapp.data.TransactionEntity
import com.anderson.financeapp.repository.UserRepository
import kotlinx.coroutines.launch

@Composable
fun DashboardScreen(
    userId: Int,
    userName: String,
    onRecharge: () -> Unit,
    onIncome: () -> Unit,
    onExpense: () -> Unit,
    onTransfer: () -> Unit,
    onHistory: () -> Unit,
    onProfile: () -> Unit,
    onAccounts: () -> Unit,
    onCreateAccount: () -> Unit,
    onEducation: () -> Unit,
    onSettings: () -> Unit,
    onLogout: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val repository = remember {
        UserRepository(AppDatabase.getDatabase(context).financeDao())
    }

    var balance by remember { mutableStateOf(0.0) }
    var totalAccounts by remember { mutableStateOf(0) }
    var lastMovements by remember { mutableStateOf<List<TransactionEntity>>(emptyList()) }

    LaunchedEffect(userId) {
        scope.launch {
            balance = repository.getTotalBalance(userId)
            totalAccounts = repository.getUserAccounts(userId).size
            lastMovements = AppDatabase.getDatabase(context)
                .financeDao()
                .getTransactions(userId)
                .take(3)
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
            .padding(22.dp)
    ) {
        item {
            Text(
                text = "Hola $userName 👋",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0F293D)
            )

            Text(
                text = "Bienvenido a FINANCE",
                fontSize = 15.sp,
                color = Color(0xFF526173)
            )

            Spacer(modifier = Modifier.height(22.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(28.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.linearGradient(
                                colors = listOf(
                                    Color(0xFF2563EB),
                                    Color(0xFF0F76B8)
                                )
                            )
                        )
                        .padding(24.dp)
                ) {
                    Column {
                        Text(
                            text = "Balance total",
                            fontSize = 16.sp,
                            color = Color.White
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "L ${String.format("%.2f", balance)}",
                            fontSize = 38.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "$totalAccounts cuentas activas",
                            fontSize = 14.sp,
                            color = Color(0xFFEAF4FF)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Acciones rápidas",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0F293D)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                DashboardActionCard("💰", "Recargar", onRecharge, Modifier.weight(1f))
                Spacer(modifier = Modifier.width(10.dp))
                DashboardActionCard("💸", "Enviar", onTransfer, Modifier.weight(1f))
                Spacer(modifier = Modifier.width(10.dp))
                DashboardActionCard("🏦", "Cuentas", onAccounts, Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(22.dp))

            Text(
                text = "Movimientos",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0F293D)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                DashboardWideCard("📈", "Ingresos", onIncome, Modifier.weight(1f))
                Spacer(modifier = Modifier.width(10.dp))
                DashboardWideCard("📉", "Gastos", onExpense, Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(10.dp))

            Spacer(modifier = Modifier.height(22.dp))

            Text(
                text = "Últimos movimientos",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0F293D)
            )

            Spacer(modifier = Modifier.height(12.dp))

            if (lastMovements.isEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Text(
                        text = "Aún no tienes movimientos registrados.",
                        modifier = Modifier.padding(18.dp),
                        color = Color(0xFF526173)
                    )
                }
            } else {
                lastMovements.forEach { movement ->
                    RecentMovementCard(movement)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 14.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {

                    BottomNavItem(
                        icon = "🏠",
                        text = "Inicio",
                        onClick = {}
                    )

                    BottomNavItem(
                        icon = "📋",
                        text = "Movimientos",
                        onClick = onHistory
                    )

                    BottomNavItem(
                        icon = "➕",
                        text = "Crear Cuenta",
                        onClick = onCreateAccount
                    )

                    BottomNavItem(
                        icon = "⚙️",
                        text = "Ajustes",
                        onClick = onSettings
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
fun DashboardActionCard(
    icon: String,
    title: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier.height(96.dp),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(icon, fontSize = 26.sp)
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0F293D),
                fontSize = 13.sp
            )
        }
    }
}

@Composable
fun DashboardWideCard(
    icon: String,
    title: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier.height(72.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(icon, fontSize = 24.sp)
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0F293D)
            )
        }
    }
}

@Composable
fun RecentMovementCard(movement: TransactionEntity) {
    val icon = when {
        movement.type.contains("INGRESO") -> "📈"
        movement.type.contains("GASTO") -> "📉"
        movement.type.contains("RECARGA") -> "💰"
        movement.type.contains("RECIBIDA") -> "✅"
        movement.type.contains("ENVIADA") -> "💸"
        movement.type.contains("TRANSFERENCIA") -> "💸"
        else -> "📋"
    }

    val amountText = when {
        movement.type.contains("GASTO") ||
                movement.type.contains("ENVIADA") ||
                movement.type.contains("TRANSFERENCIA") -> "- L ${String.format("%.2f", movement.amount)}"
        else -> "+ L ${String.format("%.2f", movement.amount)}"
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(icon, fontSize = 24.sp)

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = movement.title,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F293D)
                )

                Text(
                    text = movement.date,
                    fontSize = 13.sp,
                    color = Color(0xFF526173)
                )
            }

            Text(
                text = amountText,
                fontWeight = FontWeight.Bold,
                color = if (amountText.startsWith("+")) Color(0xFF16A34A) else Color(0xFFEF4444)
            )
        }
    }
}

@Composable
fun BottomNavItem(
    icon: String,
    text: String,
    onClick: () -> Unit
) {
    TextButton(
        onClick = onClick
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = icon,
                fontSize = 22.sp
            )

            Text(
                text = text,
                fontSize = 12.sp,
                color = Color(0xFF0F293D)
            )
        }
    }
}