package com.anderson.financeapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.anderson.financeapp.data.AppDatabase
import com.anderson.financeapp.repository.UserRepository
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    onGoToRegister: () -> Unit,
    onForgotPassword: () -> Unit,
    onLoginSuccess: (Int, String) -> Unit
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val scope = rememberCoroutineScope()

    val repository = remember {
        UserRepository(
            AppDatabase.getDatabase(context).financeDao()
        )
    }

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
            .padding(28.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(60.dp))

        Image(
            painter = painterResource(id = R.drawable.logo_financiero),
            contentDescription = "Logo",
            modifier = Modifier
                .fillMaxWidth(0.55f)
                .height(120.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Iniciar sesión",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF0F293D)
        )

        Text(
            text = "Accede a tu billetera financiera",
            fontSize = 15.sp,
            color = Color(0xFF526173)
        )

        Spacer(modifier = Modifier.height(30.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            )
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Correo electrónico") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Contraseña") },
                    modifier = Modifier.fillMaxWidth()
                )

                if (message.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = message,
                        color = Color.Red
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                TextButton(
                    onClick = onForgotPassword,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        "¿Olvidaste tu contraseña?",
                        color = Color(0xFF2563EB)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Button(
                    onClick = {
                        if (email.isBlank() || password.isBlank()) {
                            message = "Ingresa correo y contraseña"
                        } else {
                            scope.launch {

                                val result = repository.loginUser(
                                    email = email.trim(),
                                    password = password.trim()
                                )

                                if (result.isSuccess) {

                                    val user = result.getOrNull()

                                    if (user != null) {
                                        onLoginSuccess(
                                            user.id,
                                            user.fullName
                                        )
                                    }

                                } else {
                                    message = result.exceptionOrNull()?.message
                                        ?: "Error al iniciar sesión"
                                }
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF2563EB)
                    )
                ) {
                    Text("INGRESAR")
                }

                Spacer(modifier = Modifier.height(12.dp))

                TextButton(
                    onClick = onGoToRegister,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        "¿No tienes cuenta? Regístrate",
                        color = Color(0xFF2563EB)
                    )
                }
            }
        }
    }
}