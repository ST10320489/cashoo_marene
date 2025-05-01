package com.iie.st10320489.marene.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "User")
data class User(
    @PrimaryKey(autoGenerate = true) val userId: Int = 0,
    val name: String,
    val surname: String,
    val email: String,
    val password: String,
    var cashoos: Double,
    val isActive: Boolean
)
