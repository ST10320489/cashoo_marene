package com.iie.st10320489.marene.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey

@Entity(
    tableName = "RewardHistory",
    foreignKeys = [
        ForeignKey(entity = User::class, parentColumns = ["userId"], childColumns = ["userId"]),
        ForeignKey(entity = Reward::class, parentColumns = ["rewardId"], childColumns = ["rewardId"])
    ]
)
data class RewardHistory(
    @PrimaryKey(autoGenerate = true) val historyId: Int = 0,
    val userId: Int,
    val rewardId: Int,
    val dateRedeemed: String,
    val amount: Double
)
