package com.iie.st10320489.marene.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.iie.st10320489.marene.data.entities.User
import kotlinx.coroutines.flow.Flow


@Dao
interface UserDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User) // for Kotlin coroutines

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUserNow(user: User) // for Java / no coroutines

    @Query("SELECT * FROM User WHERE userID = :id")
    fun getUserById(id: Int): Flow<User?>

    @Query("SELECT * FROM User")
    fun getAllUsers(): Flow<List<User>>

    @Query("SELECT * FROM User")
    fun getAllUsersNow(): List<User>

    @Query("SELECT * FROM User WHERE email = :email LIMIT 1")
    fun findUserByEmail(email: String?): User?

    @Query("SELECT userId FROM User WHERE email = :email")
    suspend fun getUserIdByEmail(email: String): Int?

    @Update
    suspend fun updateUser(user: User)

    @Delete
    suspend fun deleteUser(user: User)

    //delete all from table
    @Query("DELETE FROM User")
    fun deleteAllTableName()

    @Query("SELECT name FROM User WHERE email = :email LIMIT 1")
    suspend fun getUserNameByEmail(email: String): String?
}

