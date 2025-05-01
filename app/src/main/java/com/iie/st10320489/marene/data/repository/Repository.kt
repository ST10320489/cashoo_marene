package com.iie.st10320489.marene.data.repository

import androidx.lifecycle.LiveData
import com.iie.st10320489.marene.data.dao.*
import com.iie.st10320489.marene.data.entities.*
import kotlinx.coroutines.flow.Flow

class UserRepository(private val userDao: UserDao) {
    suspend fun insertUser(user: User) = userDao.insertUser(user)
    fun getAllUsers(): Flow<List<User>> = userDao.getAllUsers()
    fun getUserById(id: Int): Flow<User?> = userDao.getUserById(id)
    suspend fun updateUser(user: User) = userDao.updateUser(user)
    suspend fun deleteUser(user: User) = userDao.deleteUser(user)
    suspend fun getUserIdByEmail(email: String): Int? {
        return userDao.getUserIdByEmail(email)
    }

}

class UserSettingsRepository(private val dao: UserSettingsDao) {

    suspend fun insertUserSettings(userSettings: UserSettings) {
        dao.insertUserSettings(userSettings)
    }

    suspend fun getUserSettingsByUserId(userId: Int): UserSettings? {
        return dao.getUserSettingsByUserId(userId)
    }

    fun getAllSettings(): Flow<List<UserSettings>> {
        return dao.getAllSettings()
    }

    suspend fun updateUserSettings(userSettings: UserSettings) {
        dao.updateUserSettings(userSettings)
    }

    suspend fun deleteUserSettings(userSettings: UserSettings) {
        dao.deleteSettings(userSettings)
    }

    suspend fun deleteAllSettings() {
        dao.deleteAllTableName()
    }
}

class TransactionRepository(private val dao: TransactionDao) {
    suspend fun insert(transaction: Transaction) = dao.insertTransaction(transaction)
    fun getAll(): Flow<List<Transaction>> = dao.getAllTransactions()
    // Correct method to get transaction by both userId and transactionId
    fun getById(userId: Int, transactionId: Int): Flow<Transaction?> = dao.getTransactionById(userId, transactionId)

    suspend fun update(transaction: Transaction) = dao.updateTransaction(transaction)
    suspend fun delete(transaction: Transaction) = dao.deleteTransaction(transaction)
    suspend fun getAllTransactions(): List<TransactionWithCategory> {
        return dao.getAllTransactionsWithCategory()
    }
    suspend fun getTransactionsByUserId(userId: Int): List<TransactionWithCategory> {
        return dao.getTransactionsByUserId(userId)
    }

}

class CategoryRepository(private val dao: CategoryDao) {
    suspend fun insert(category: Category) = dao.insertCategory(category)
    fun getAll(): LiveData<List<Category>> = dao.getAllCategories()
    fun getById(id: Int): Flow<Category?> = dao.getCategoryById(id)
    suspend fun delete(category: Category) = dao.deleteCategory(category)
    fun getCategoriesByUser(userId: Int): LiveData<List<Category>> {
        return dao.getCategoriesByUser(userId)
    }

}

class SubCategoryRepository(private val dao: SubCategoryDao) {
    suspend fun insert(subCategory: SubCategory) = dao.insertSubCategory(subCategory)
    fun getAll(): Flow<List<SubCategory>> = dao.getAllSubCategories()
    fun getById(id: Int): Flow<SubCategory?> = dao.getSubCategoryById(id)
    suspend fun delete(subCategory: SubCategory) = dao.deleteSubCategory(subCategory)
}

class RewardRepository(private val dao: RewardDao) {
    suspend fun insert(reward: Reward) = dao.insertReward(reward)
    fun getAll(): Flow<List<Reward>> = dao.getAllRewards()
    fun getById(id: Int): Flow<Reward?> = dao.getRewardById(id)
    suspend fun update(reward: Reward) = dao.updateReward(reward)
    suspend fun delete(reward: Reward) = dao.deleteReward(reward)
}

class RewardHistoryRepository(private val dao: RewardHistoryDao) {
    suspend fun insert(history: RewardHistory) = dao.insertHistory(history)
    fun getAll(): Flow<List<RewardHistory>> = dao.getAllHistory()
    fun getById(id: Int): Flow<RewardHistory?> = dao.getHistoryById(id)
    suspend fun delete(history: RewardHistory) = dao.deleteHistory(history)
}

class ActivityLogRepository(private val dao: ActivityLogDao) {
    suspend fun insert(log: ActivityLog) = dao.insertLog(log)
    fun getAll(): Flow<List<ActivityLog>> = dao.getAllLogs()
    fun getById(id: Int): Flow<ActivityLog?> = dao.getLogById(id)
    suspend fun delete(log: ActivityLog) = dao.deleteLog(log)
}

