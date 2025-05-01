package com.iie.st10320489.marene.data.dao

import androidx.room.*
import com.iie.st10320489.marene.data.entities.Reward
import kotlinx.coroutines.flow.Flow

@Dao
interface RewardDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReward(reward: Reward)

    @Query("SELECT * FROM Reward WHERE rewardID = :id")
    fun getRewardById(id: Int): Flow<Reward?>

    @Query("SELECT * FROM Reward")
    fun getAllRewards(): Flow<List<Reward>>

    @Update
    suspend fun updateReward(reward: Reward)

    @Delete
    suspend fun deleteReward(reward: Reward)

    //delete all from table
    @Query("DELETE FROM Reward")
    fun deleteAllTableName()
}
