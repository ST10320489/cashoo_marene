package com.iie.st10320489.marene.data.dao

import androidx.room.*
import com.iie.st10320489.marene.data.entities.ActivityLog
import kotlinx.coroutines.flow.Flow

@Dao
interface ActivityLogDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLog(log: ActivityLog)

    @Query("SELECT * FROM ActivityLog WHERE logID = :id")
    fun getLogById(id: Int): Flow<ActivityLog?>

    @Query("SELECT * FROM ActivityLog")
    fun getAllLogs(): Flow<List<ActivityLog>>

    @Delete
    suspend fun deleteLog(log: ActivityLog)

    //delete all from table
    @Query("DELETE FROM ActivityLog")
    fun deleteAllTableName()
}
