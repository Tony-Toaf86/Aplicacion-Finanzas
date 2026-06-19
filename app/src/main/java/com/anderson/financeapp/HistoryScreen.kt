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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import com.anderson.financeapp.data.AppDatabase
import com.anderson.financeapp.data.TransactionEntity
import kotlinx.coroutines.launch

@Composable
fun HistoryScreen(userId: Int, onBack: () -> Unit) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val dao = remember { AppDatabase.getDatabase(context).financeDao() }

    var movements by remember { mutableStateOf<List<TransactionEntity>>(emptyList()) }
    var selectedFilter by remember { mutableStateOf("Todos") }

    LaunchedEffect(userId) {
        scope.launch {
            movements = dao.getTransactions(userId)
        }
    }

    val filteredMovements = when (selectedFilter) {
        "Ingresos" -> movements.filter {
            it.type.contains("INGRESO") || it.type.contains("RECARGA") || it.type.contains("RECIBIDA")
        }
        "Gastos" -> movements.filter {
            it.type.contains("GASTO")
        }
        "Transferencias" -> movements.filter {
            it.type.contains("TRANSFERENCIA") || it.type.contains("ENVIADA") || it.type.contains("RECIBIDA")
        }
        else -> movements
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
            .padding(24.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .background(Color(0xFFEAF4FF), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text("📋", fontSize = 26.sp)
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column {
                Text(
                    text = "Historial",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F293D)
                )

                Text(
                    text = "Tus movimientos financieros",
                    fontSize = 15.sp,
                    color = Color(0xFF526173)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                HistoryFilterButton("Todos", selectedFilter) {
                    selectedFilter = "Todos"
                }

                HistoryFilterButton("Ingresos", selectedFilter) {
                    selectedFilter = "Ingresos"
                }

                HistoryFilterButton("Gastos", selectedFilter) {
                    selectedFilter = "Gastos"
                }

                HistoryFilterButton("Transf.", selectedFilter) {
                    selectedFilter = "Transferencias"
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        if (filteredMovements.isEmpty()) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No hay movimientos en esta categoría.",
                        color = Color(0xFF526173)
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {
                items(filteredMovements) { movement ->
                    HistoryMovementCard(movement)
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = onBack,
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF2563EB),
                contentColor = Color.White
            )
        ) {
            Text(
                text = "VOLVER",
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun HistoryFilterButton(
    text: String,
    selectedFilter: String,
    onClick: () -> Unit
) {
    val selected = when (text) {
        "Transf." -> selectedFilter == "Transferencias"
        else -> selectedFilter == text
    }

    Button(
        onClick = onClick,
        shape = RoundedCornerShape(14.dp),
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (selected) Color(0xFF2563EB) else Color(0xFFEAF4FF),
            contentColor = if (selected) Color.White else Color(0xFF2563EB)
        )
    ) {
        Text(
            text = text,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun HistoryMovementCard(movement: TransactionEntity) {
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

    val amountColor = if (amountText.startsWith("+")) {
        Color(0xFF16A34A)
    } else {
        Color(0xFFEF4444)
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(Color(0xFFEAF4FF), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(icon, fontSize = 24.sp)
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = movement.title,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F293D)
                )

                Spacer(modifier = Modifier.height(3.dp))

                Text(
                    text = "${movement.type} · ${movement.date}",
                    fontSize = 13.sp,
                    color = Color(0xFF526173)
                )
            }

            Text(
                text = amountText,
                fontWeight = FontWeight.Bold,
                color = amountColor
            )
        }
    }
}