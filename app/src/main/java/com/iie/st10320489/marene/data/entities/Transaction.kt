package com.iie.st10320489.marene.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey

@Entity(
    tableName = "Transaction",
    foreignKeys = [
        ForeignKey(entity = User::class, parentColumns = ["userId"], childColumns = ["userId"], onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = Category::class, parentColumns = ["categoryId"], childColumns = ["categoryId"]),
        ForeignKey(entity = SubCategory::class, parentColumns = ["subCategoryId"], childColumns = ["subCategoryId"])
    ]
)
data class Transaction(
    @PrimaryKey(autoGenerate = true) val transactionId: Int = 0,
    val userId: Int,
    val name: String,
    val amount: Double,
    val transactionMethod: String,
    val location: String?,
    val dateTime: String,
    val description: String,
    val photo: String,
    val categoryId: Int,
    val subCategoryId: Int?,
    val expense: Boolean,
    val recurring: Boolean
)
