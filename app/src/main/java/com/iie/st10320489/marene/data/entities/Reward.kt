package com.iie.st10320489.marene.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Reward")
data class Reward(
    @PrimaryKey(autoGenerate = true) val rewardId: Int = 0,
    val name: String,
    val description: String,
    val amount: Double,
    val type: String,
    val code: Int
)
