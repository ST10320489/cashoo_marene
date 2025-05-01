package com.iie.st10320489.marene.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase
import com.iie.st10320489.marene.data.dao.ActivityLogDao
import com.iie.st10320489.marene.data.dao.CategoryDao
import com.iie.st10320489.marene.data.dao.RewardDao
import com.iie.st10320489.marene.data.dao.RewardHistoryDao
import com.iie.st10320489.marene.data.dao.SubCategoryDao
import com.iie.st10320489.marene.data.dao.TransactionDao
import com.iie.st10320489.marene.data.dao.UserDao
import com.iie.st10320489.marene.data.dao.UserSettingsDao
import com.iie.st10320489.marene.data.entities.ActivityLog
import com.iie.st10320489.marene.data.entities.Category
import com.iie.st10320489.marene.data.entities.Reward
import com.iie.st10320489.marene.data.entities.RewardHistory
import com.iie.st10320489.marene.data.entities.SubCategory
import com.iie.st10320489.marene.data.entities.Transaction
import com.iie.st10320489.marene.data.entities.User
import com.iie.st10320489.marene.data.entities.UserSettings


@Database(
    entities = [
        User::class,
        UserSettings::class,
        Transaction::class,
        Category::class,
        SubCategory::class,
        Reward::class,
        RewardHistory::class,
        ActivityLog::class
    ],
    version = 5,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun userSettingsDao(): UserSettingsDao
    abstract fun transactionDao(): TransactionDao
    abstract fun categoryDao(): CategoryDao
    abstract fun subCategoryDao(): SubCategoryDao
    abstract fun rewardDao(): RewardDao
    abstract fun rewardHistoryDao(): RewardHistoryDao
    abstract fun activityLogDao(): ActivityLogDao


}
