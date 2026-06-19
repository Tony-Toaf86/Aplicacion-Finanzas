package com.anderson.financeapp.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface FinanceDao {

    @Insert
    suspend fun insertUser(user: UserEntity): Long

    @Query("SELECT * FROM users WHERE email = :email AND password = :password LIMIT 1")
    suspend fun login(email: String, password: String): UserEntity?

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun findUserByEmail(email: String): UserEntity?

    @Insert
    suspend fun insertAccount(account: AccountEntity): Long

    @Query("SELECT * FROM accounts WHERE userId = :userId")
    suspend fun getAccounts(userId: Int): List<AccountEntity>

    @Insert
    suspend fun insertTransaction(transaction: TransactionEntity): Long

    @Query("SELECT * FROM transactions WHERE userId = :userId ORDER BY id DESC")
    suspend fun getTransactions(userId: Int): List<TransactionEntity>

    @Query("SELECT * FROM accounts WHERE userId = :userId LIMIT 1")
    suspend fun getMainAccount(userId: Int): AccountEntity?

    @Query("UPDATE accounts SET balance = :newBalance WHERE userId = :userId")
    suspend fun updateBalance(userId: Int, newBalance: Double)

    @Query("SELECT balance FROM accounts WHERE userId = :userId LIMIT 1")
    suspend fun getBalanceValue(userId: Int): Double?

    @Query("UPDATE users SET password = :newPassword WHERE email = :email")
    suspend fun updatePassword(email: String, newPassword: String): Int

    @Query("SELECT * FROM accounts WHERE id = :accountId LIMIT 1")
    suspend fun getAccountById(accountId: Int): AccountEntity?

    @Query("UPDATE accounts SET balance = :newBalance WHERE id = :accountId")
    suspend fun updateBalanceByAccountId(accountId: Int, newBalance: Double)

    @Query("SELECT * FROM users WHERE id = :userId LIMIT 1")
    suspend fun getUserById(userId: Int): UserEntity?

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): UserEntity?

    @Query("SELECT * FROM accounts WHERE userId = :userId")
    suspend fun getAccountsByUserId(userId: Int): List<AccountEntity>
}