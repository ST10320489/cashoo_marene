package com.iie.st10320489.marene.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey

@Entity(
    tableName = "ActivityLog",
    foreignKeys = [ForeignKey(
        entity = User::class,
        parentColumns = ["userId"],
        childColumns = ["userId"]
    )]
)
data class ActivityLog(
    @PrimaryKey(autoGenerate = true) val logId: Int = 0,
    val userId: Int,
    val action: String,
    val timestamp: String,
    val details: String?
)
