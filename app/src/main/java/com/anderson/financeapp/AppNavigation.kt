package com.anderson.financeapp

import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable

@Composable
fun AppNavigation() {

    var currentScreen by rememberSaveable { mutableStateOf("welcome") }
    var activeUserId by rememberSaveable { mutableStateOf(0) }
    var activeUserName by rememberSaveable { mutableStateOf("") }

    when (currentScreen) {

        "welcome" -> WelcomeScreen(
            onStart = { currentScreen = "login" }
        )

        "login" -> LoginScreen(
            onGoToRegister = {
                currentScreen = "register"
            },

            onForgotPassword = {
                currentScreen = "forgot"
            },

            onLoginSuccess = { userId, fullName ->
                activeUserId = userId
                activeUserName = fullName
                currentScreen = "dashboard"
            }
        )

        "forgot" -> ForgotPasswordScreen(
            onBackToLogin = {
                currentScreen = "login"
            }
        )

        "accounts" -> AccountsScreen(
            userId = activeUserId,
            onCreateAccount = { currentScreen = "create_account" },
            onBack = { currentScreen = "dashboard" }
        )

        "create_account" -> CreateAccountScreen(
            userId = activeUserId,
            onAccountCreated = { currentScreen = "accounts" },
            onBack = { currentScreen = "accounts" }
        )

        "register" -> RegisterScreen(
            onGoToLogin = {
                currentScreen = "login"
            },

            onRegisterSuccess = {
                currentScreen = "login"
            }
        )

        "dashboard" -> DashboardScreen(
            userId = activeUserId,
            userName = activeUserName,
            onRecharge = { currentScreen = "recharge" },
            onIncome = { currentScreen = "income" },
            onExpense = { currentScreen = "expense" },
            onTransfer = { currentScreen = "transfer" },
            onHistory = { currentScreen = "history" },
            onProfile = { currentScreen = "profile" },
            onEducation = { currentScreen = "education" },
            onSettings = { currentScreen = "settings" },
            onAccounts = { currentScreen = "accounts" },
            onCreateAccount = { currentScreen = "create_account" },
            onLogout = {
                activeUserId = 0
                activeUserName = ""
                currentScreen = "login"
            }
        )

        "recharge" -> RechargeScreen(
            userId = activeUserId,
            onBack = { currentScreen = "dashboard" }
        )

        "income" -> IncomeScreen(
            userId = activeUserId,
            onBack = { currentScreen = "dashboard" }
        )

        "expense" -> ExpenseScreen(
            userId = activeUserId,
            onBack = { currentScreen = "dashboard" }
        )

        "transfer" -> TransferScreen(
            userId = activeUserId,
            onBack = { currentScreen = "dashboard" }
        )

        "history" -> HistoryScreen(
            userId = activeUserId,
            onBack = { currentScreen = "dashboard" }
        )

        "profile" -> ProfileScreen(
            userId = activeUserId,
            userName = activeUserName,
            onBack = { currentScreen = "settings" }
        )

        "education" -> EducationScreen(
            onBack = { currentScreen = "dashboard" }
        )

        "settings" -> SettingsScreen(
            onBack = { currentScreen = "dashboard" },
            onProfile = { currentScreen = "profile" },
            onChangePassword = { currentScreen = "forgot" },
            onEducation = { currentScreen = "education" },
            onLogout = {
                activeUserId = 0
                activeUserName = ""
                currentScreen = "login"
            }
        )
    }
}