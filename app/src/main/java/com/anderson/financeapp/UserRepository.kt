package com.anderson.financeapp.repository

import com.anderson.financeapp.data.AccountEntity
import com.anderson.financeapp.data.FinanceDao
import com.anderson.financeapp.data.UserEntity

class UserRepository(
    private val dao: FinanceDao
) {
    suspend fun registerUser(
        fullName: String,
        email: String,
        password: String
    ): Result<UserEntity> {

        val existingUser = dao.findUserByEmail(email)

        if (existingUser != null) {
            return Result.failure(Exception("Este correo ya está registrado"))
        }

        val userId = dao.insertUser(
            UserEntity(
                fullName = fullName,
                email = email,
                password = password
            )
        ).toInt()

        dao.insertAccount(
            AccountEntity(
                userId = userId,
                accountName = "Cuenta principal",
                balance = 0.00,
                accountType = "BANCO",
                cardNumber = ""
            )
        )

        return Result.success(
            UserEntity(
                id = userId,
                fullName = fullName,
                email = email,
                password = password
            )
        )
    }

    suspend fun loginUser(
        email: String,
        password: String
    ): Result<UserEntity> {

        val user = dao.login(email, password)

        return if (user != null) {
            Result.success(user)
        } else {
            Result.failure(Exception("Correo o contraseña incorrectos"))
        }
    }
    suspend fun getUserBalance(userId: Int): Double {
        val account = dao.getMainAccount(userId)
        return account?.balance ?: 0.0
    }
    suspend fun addMoney(
        userId: Int,
        title: String,
        amount: Double,
        type: String
    ): Result<Unit> {
        if (amount <= 0) {
            return Result.failure(Exception("El monto debe ser mayor que cero"))
        }

        val currentBalance = dao.getBalanceValue(userId) ?: 0.0
        val newBalance = currentBalance + amount

        dao.updateBalance(userId, newBalance)

        dao.insertTransaction(
            com.anderson.financeapp.data.TransactionEntity(
                userId = userId,
                title = title,
                amount = amount,
                type = type,
                date = java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault())
                    .format(java.util.Date())
            )
        )

        return Result.success(Unit)
    }

    suspend fun subtractMoney(
        userId: Int,
        title: String,
        amount: Double,
        type: String
    ): Result<Unit> {
        if (amount <= 0) {
            return Result.failure(Exception("El monto debe ser mayor que cero"))
        }

        val currentBalance = dao.getBalanceValue(userId) ?: 0.0

        if (amount > currentBalance) {
            return Result.failure(Exception("Saldo insuficiente"))
        }

        val newBalance = currentBalance - amount

        dao.updateBalance(userId, newBalance)

        dao.insertTransaction(
            com.anderson.financeapp.data.TransactionEntity(
                userId = userId,
                title = title,
                amount = amount,
                type = type,
                date = java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault())
                    .format(java.util.Date())
            )
        )

        return Result.success(Unit)
    }
    suspend fun resetPassword(
        email: String,
        newPassword: String
    ): Result<Unit> {
        val user = dao.findUserByEmail(email)

        if (user == null) {
            return Result.failure(Exception("No existe una cuenta con ese correo"))
        }

        dao.updatePassword(email, newPassword)

        return Result.success(Unit)
    }
    suspend fun createAccount(
        userId: Int,
        accountName: String,
        initialBalance: Double,
        accountType: String
    ): Result<Unit> {
        if (accountName.isBlank()) {
            return Result.failure(Exception("Ingresa el nombre de la cuenta"))
        }

        if (initialBalance < 0) {
            return Result.failure(Exception("El saldo inicial no puede ser negativo"))
        }

        val generatedCardNumber = if (accountType == "TARJETA") {
            "4" + (100000000000000L..999999999999999L).random().toString()
        } else {
            ""
        }

        dao.insertAccount(
            AccountEntity(
                userId = userId,
                accountName = accountName,
                balance = initialBalance,
                accountType = accountType,
                cardNumber = generatedCardNumber
            )
        )

        return Result.success(Unit)
    }

    suspend fun getUserAccounts(userId: Int): List<AccountEntity> {
        return dao.getAccounts(userId)
    }

    suspend fun getTotalBalance(userId: Int): Double {
        val accounts = dao.getAccounts(userId)
        return accounts.sumOf { it.balance }
    }

    suspend fun findUserByEmail(
        email: String
    ): UserEntity? {
        return dao.getUserByEmail(email)
    }

    suspend fun getAccountsForUser(
        userId: Int
    ): List<AccountEntity> {
        return dao.getAccountsByUserId(userId)
    }

    suspend fun transferBetweenUsers(
        senderUserId: Int,
        senderAccountId: Int,
        receiverEmail: String,
        receiverAccountId: Int,
        amount: Double
    ): Result<Unit> {

        if (amount <= 0) {
            return Result.failure(Exception("El monto debe ser mayor que cero"))
        }

        val receiverUser = dao.getUserByEmail(receiverEmail)

        if (receiverUser == null) {
            return Result.failure(Exception("No existe un usuario con ese correo"))
        }

        if (receiverUser.id == senderUserId) {
            return Result.failure(Exception("No puedes transferirte a ti mismo por correo"))
        }

        val senderAccount = dao.getAccountById(senderAccountId)
        val receiverAccount = dao.getAccountById(receiverAccountId)

        if (senderAccount == null) {
            return Result.failure(Exception("Cuenta origen no encontrada"))
        }

        if (receiverAccount == null) {
            return Result.failure(Exception("Cuenta destino no encontrada"))
        }

        if (senderAccount.userId != senderUserId) {
            return Result.failure(Exception("La cuenta origen no pertenece al usuario actual"))
        }

        if (receiverAccount.userId != receiverUser.id) {
            return Result.failure(Exception("La cuenta destino no pertenece al usuario indicado"))
        }

        if (amount > senderAccount.balance) {
            return Result.failure(Exception("Saldo insuficiente"))
        }

        val newSenderBalance = senderAccount.balance - amount
        val newReceiverBalance = receiverAccount.balance + amount

        dao.updateBalanceByAccountId(senderAccount.id, newSenderBalance)
        dao.updateBalanceByAccountId(receiverAccount.id, newReceiverBalance)

        val today = java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault())
            .format(java.util.Date())

        dao.insertTransaction(
            com.anderson.financeapp.data.TransactionEntity(
                userId = senderUserId,
                title = "Transferencia enviada a ${receiverUser.email}",
                amount = amount,
                type = "TRANSFERENCIA ENVIADA",
                date = today
            )
        )

        dao.insertTransaction(
            com.anderson.financeapp.data.TransactionEntity(
                userId = receiverUser.id,
                title = "Transferencia recibida",
                amount = amount,
                type = "TRANSFERENCIA RECIBIDA",
                date = today
            )
        )

        return Result.success(Unit)
    }
}