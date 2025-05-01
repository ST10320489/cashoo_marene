package com.iie.st10320489.marene.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Category")
data class Category(
    @PrimaryKey(autoGenerate = true) val categoryId: Int = 0,
    val userId: Int,
    val name: String,
    val icon: Int,
    val colour: Int
)
