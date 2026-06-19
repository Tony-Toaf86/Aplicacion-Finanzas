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
fun ForgotPasswordScreen(
    onBackToLogin: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val repository = remember {
        UserRepository(AppDatabase.getDatabase(context).financeDao())
    }

    var email by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
            .padding(28.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(52.dp))

        Box(
            modifier = Modifier
                .size(82.dp)
                .background(Color(0xFFEAF4FF), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "🔐",
                fontSize = 34.sp
            )
        }

        Spacer(modifier = Modifier.height(18.dp))

        Text(
            text = "Recuperar contraseña",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF0F293D),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Actualiza tu contraseña para volver a acceder a tu cuenta FINANCE",
            fontSize = 15.sp,
            color = Color(0xFF526173),
            textAlign = TextAlign.Center,
            lineHeight = 21.sp
        )

        Spacer(modifier = Modifier.height(28.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Correo electrónico") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp)
                )

                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                    value = newPassword,
                    onValueChange = { newPassword = it },
                    label = { Text("Nueva contraseña") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp)
                )

                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    label = { Text("Confirmar contraseña") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp)
                )

                if (message.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = message,
                        color = if (message.contains("correctamente")) Color(0xFF16A34A) else Color(0xFFEF4444)
                    )
                }

                Spacer(modifier = Modifier.height(22.dp))

                Button(
                    onClick = {
                        when {
                            email.isBlank() || newPassword.isBlank() || confirmPassword.isBlank() -> {
                                message = "Completa todos los campos"
                            }

                            newPassword != confirmPassword -> {
                                message = "Las contraseñas no coinciden"
                            }

                            else -> {
                                scope.launch {
                                    val result = repository.resetPassword(
                                        email = email.trim(),
                                        newPassword = newPassword.trim()
                                    )

                                    if (result.isSuccess) {
                                        message = "✓ Contraseña actualizada correctamente"
                                        delay(1000)
                                        onBackToLogin()
                                    } else {
                                        message = result.exceptionOrNull()?.message ?: "Error al actualizar contraseña"
                                    }
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
                    Text("ACTUALIZAR CONTRASEÑA", fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(12.dp))

                TextButton(
                    onClick = onBackToLogin,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Volver al inicio de sesión", color = Color(0xFF2563EB))
                }
            }
        }
    }
}
