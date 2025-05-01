package com.iie.st10320489.marene.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey

@Entity(
    tableName = "UserSettings",
    foreignKeys = [ForeignKey(
        entity = User::class,
        parentColumns = ["userId"],
        childColumns = ["userId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class UserSettings(
    @PrimaryKey(autoGenerate = true) val userSettingsId: Int = 0,
    val userId: Int,
    val payday: String,
    val salary: Double,
    val minGoal: Double,
    val maxGoal: Double,
    var color: String,
    var chinchilla: String
)
