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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    onProfile: () -> Unit,
    onChangePassword: () -> Unit,
    onEducation: () -> Unit,
    onLogout: () -> Unit
) {
    var darkMode by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
            .padding(24.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(58.dp)
                    .background(Color(0xFFEAF4FF), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text("⚙️", fontSize = 28.sp)
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column {
                Text(
                    text = "Ajustes",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F293D)
                )

                Text(
                    text = "Administra tu cuenta FINANCE",
                    fontSize = 15.sp,
                    color = Color(0xFF526173)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                SettingsItem("👤", "Mi perfil", "Ver información de tu cuenta", onProfile)

                SettingsItem("🔐", "Cambiar contraseña", "Actualiza tu clave de acceso", onChangePassword)

                SettingsItem("🎓", "Educación financiera", "Ver video educativo", onEducation)

                SettingsSwitchItem(
                    icon = "🌙",
                    title = "Modo oscuro",
                    subtitle = "Apariencia oscura simulada",
                    checked = darkMode,
                    onCheckedChange = {
                        darkMode = it
                        message = if (it) {
                            "Modo oscuro activado visualmente"
                        } else {
                            "Modo claro activado"
                        }
                    }
                )

                SettingsItem("🔊", "Sonido", "Audio de confirmación activo") {
                    message = "Sonido activado correctamente"
                }

                SettingsItem("ℹ️", "Información", "FINANCE v1.0") {
                    message = "FINANCE v1.0 - Proyecto de Desarrollo de Aplicaciones Móviles"
                }

                SettingsItem("🚪", "Cerrar sesión", "Salir de tu cuenta", onLogout)
            }
        }

        if (message.isNotEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = message,
                color = Color(0xFF2563EB),
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.weight(1f))

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
            Text("VOLVER", fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun SettingsItem(
    icon: String,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    TextButton(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(icon, fontSize = 25.sp)

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F293D)
                )

                Text(
                    text = subtitle,
                    fontSize = 12.sp,
                    color = Color(0xFF526173)
                )
            }

            Text("›", fontSize = 26.sp, color = Color(0xFF2563EB))
        }
    }
}

@Composable
fun SettingsSwitchItem(
    icon: String,
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(icon, fontSize = 25.sp)

        Spacer(modifier = Modifier.width(14.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0F293D)
            )

            Text(
                text = subtitle,
                fontSize = 12.sp,
                color = Color(0xFF526173)
            )
        }

        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}