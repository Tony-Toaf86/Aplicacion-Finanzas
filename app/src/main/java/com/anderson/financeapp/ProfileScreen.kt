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
fun ProfileScreen(
    userId: Int,
    userName: String,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val dao = remember { AppDatabase.getDatabase(context).financeDao() }
    val repository = remember { UserRepository(dao) }

    var email by remember { mutableStateOf("") }
    var totalBalance by remember { mutableStateOf(0.0) }
    var totalAccounts by remember { mutableStateOf(0) }

    LaunchedEffect(userId) {
        scope.launch {
            val user = dao.getUserById(userId)
            email = user?.email ?: ""
            totalBalance = repository.getTotalBalance(userId)
            totalAccounts = repository.getUserAccounts(userId).size
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(28.dp))

        Box(
            modifier = Modifier
                .size(92.dp)
                .background(MaterialTheme.colorScheme.secondary, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text("👤", fontSize = 40.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = userName,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Text(
            text = email,
            fontSize = 15.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(28.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                ProfileItem("Estado", "Activo")
                ProfileItem("Tipo de cuenta", "Usuario FINANCE")
                ProfileItem("Cuentas activas", "$totalAccounts")
                ProfileItem("Balance total", "L ${String.format("%.2f", totalBalance)}")
            }
        }

        Spacer(modifier = Modifier.weight(1f))

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
fun ProfileItem(title: String, value: String) {
    Column {
        Text(
            text = title,
            fontSize = 13.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Text(
            text = value,
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(14.dp))
    }
}
