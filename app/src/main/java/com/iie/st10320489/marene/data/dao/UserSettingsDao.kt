package com.iie.st10320489.marene.data.dao

import androidx.room.*
import com.iie.st10320489.marene.data.entities.UserSettings
import kotlinx.coroutines.flow.Flow

@Dao
interface UserSettingsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserSettings(userSettings: UserSettings)

    @Query("SELECT * FROM UserSettings WHERE userId = :userId LIMIT 1")
    suspend fun getUserSettingsByUserId(userId: Int): UserSettings?

    @Query("SELECT * FROM UserSettings")
    fun getAllSettings(): Flow<List<UserSettings>>

    @Update
    suspend fun updateUserSettings(userSettings: UserSettings)

    @Delete
    suspend fun deleteSettings(userSettings: UserSettings)

    @Query("DELETE FROM UserSettings")
    fun deleteAllTableName()


}
