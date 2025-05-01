package com.iie.st10320489.marene.data.repository

import com.iie.st10320489.marene.data.dao.ActivityLogDao
import com.iie.st10320489.marene.data.dao.CategoryDao
import com.iie.st10320489.marene.data.dao.RewardDao
import com.iie.st10320489.marene.data.dao.RewardHistoryDao
import com.iie.st10320489.marene.data.dao.SubCategoryDao
import com.iie.st10320489.marene.data.dao.TransactionDao
import com.iie.st10320489.marene.data.dao.UserDao
import com.iie.st10320489.marene.data.dao.UserSettingsDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DatabaseCleaner(
    private val userDao: UserDao,
    private val userSettingsDao: UserSettingsDao,
    private val transactionDao: TransactionDao,
    private val categoryDao: CategoryDao,
    private val subCategoryDao: SubCategoryDao,
    private val rewardDao: RewardDao,
    private val rewardHistoryDao: RewardHistoryDao,
    private val activityLogDao: ActivityLogDao
) {
    suspend fun clearAllData() {
        withContext(Dispatchers.IO) {
            userDao.deleteAllTableName()
            userSettingsDao.deleteAllTableName()
            transactionDao.deleteAllTableName()
            categoryDao.deleteAllTableName()
            subCategoryDao.deleteAllTableName()
            rewardDao.deleteAllTableName()
            rewardHistoryDao.deleteAllTableName()
            activityLogDao.deleteAllTableName()
        }
    }
}