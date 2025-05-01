package com.iie.st10320489.marene.data.dao

import androidx.room.*
import com.iie.st10320489.marene.data.entities.RewardHistory
import kotlinx.coroutines.flow.Flow

@Dao
interface RewardHistoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHistory(rewardHistory: RewardHistory)

    @Query("SELECT * FROM RewardHistory WHERE historyId = :id")
    fun getHistoryById(id: Int): Flow<RewardHistory?>

    @Query("SELECT * FROM RewardHistory")
    fun getAllHistory(): Flow<List<RewardHistory>>

    @Delete
    suspend fun deleteHistory(rewardHistory: RewardHistory)

    //delete all from table
    @Query("DELETE FROM RewardHistory")
    fun deleteAllTableName()
}