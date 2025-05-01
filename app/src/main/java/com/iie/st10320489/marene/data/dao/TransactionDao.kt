package com.iie.st10320489.marene.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.iie.st10320489.marene.data.entities.Transaction
import com.iie.st10320489.marene.data.entities.TransactionWithCategory
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: Transaction)

    @Query("SELECT * FROM `Transaction` WHERE userId = :userId ORDER BY dateTime DESC LIMIT 5")
    suspend fun getTop5RecentTransactionsByUserId(userId: Int): List<TransactionWithCategory>

    @Query("SELECT * FROM `Transaction` WHERE userId = :userId")
    suspend fun getTransactionsByUser(userId: Int): List<TransactionWithCategory>

    @Insert
    suspend fun insert(transaction: Transaction)

    @Query("SELECT * FROM `Transaction` WHERE userId = :userId AND transactionId = :transactionId LIMIT 1")
    fun getTransactionById(userId: Int, transactionId: Int): Flow<Transaction?>


    @Query("SELECT * FROM `Transaction`")
    fun getAllTransactions(): Flow<List<Transaction>>

    @Update
    suspend fun updateTransaction(transaction: Transaction)

    @Delete
    suspend fun deleteTransaction(transaction: Transaction)

    //delete all from table
    @Query("DELETE FROM `Transaction`")
    fun deleteAllTableName()


    @Query("SELECT * FROM `Transaction` ORDER BY dateTime DESC")
    suspend fun getAllTransactionsWithCategory(): List<TransactionWithCategory>

    @Query("SELECT * FROM `Transaction` WHERE userId = :userId ORDER BY dateTime DESC")
    suspend fun getTransactionsByUserId(userId: Int): List<TransactionWithCategory>

    @Query("SELECT * FROM `Transaction` WHERE userId = :userId AND categoryId = :categoryId")
    fun getTransactionsByUserAndCategory(userId: Int, categoryId: Int): LiveData<List<Transaction>>

    @Query("SELECT * FROM `Transaction` WHERE transactionId = :id")
    suspend fun getTransactionWithCategoryById(id: Int): TransactionWithCategory?

    @Query("""
    SELECT `Transaction`.* FROM `Transaction`
    INNER JOIN Category ON `Transaction`.categoryId = Category.categoryID
    WHERE `Transaction`.userId = :userId
    AND category.name = :categoryName
""")
    suspend fun getTransactionsByUserIdAndCategoryName(userId: Int, categoryName: String): List<TransactionWithCategory>

    @Query("""
    SELECT * FROM `Transaction`
    INNER JOIN SubCategory ON `Transaction`.subCategoryId = SubCategory.subCategoryId
    WHERE `Transaction`.userId = :userId AND SubCategory.name = :subCategoryName
""")
    suspend fun getTransactionsByUserIdAndSubCategoryName(userId: Int, subCategoryName: String): List<TransactionWithCategory>


    @Query("""
    SELECT * FROM `Transaction` 
    WHERE userId = :userId 
      AND strftime('%m', dateTime) = :month 
      AND strftime('%Y', dateTime) = :year 
      AND expense = 0
""")
    suspend fun getMonthlyIncome(userId: Int, month: String, year: String): List<Transaction>

    @Query("""
    SELECT * FROM `Transaction` 
    WHERE userId = :userId 
      AND strftime('%m', dateTime) = :month 
      AND strftime('%Y', dateTime) = :year 
      AND expense = 1
""")
    suspend fun getMonthlyExpenses(userId: Int, month: String, year: String): List<Transaction>

    @Query("SELECT * FROM `Transaction` WHERE userId = :userId AND expense = 1")
    suspend fun getTransactionsWithCategory(userId: Int): List<TransactionWithCategory>

    @Query("""
    SELECT t.* FROM `Transaction` t
    INNER JOIN Category c ON t.categoryId = c.categoryID
    WHERE t.userId = :userId 
      AND strftime('%m', t.dateTime) = :month 
      AND strftime('%Y', t.dateTime) = :year 
      AND c.name = 'Savings'
""")
    suspend fun getMonthlySavings(userId: Int, month: String, year: String): List<Transaction>


}