package com.iie.st10320489.marene.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "SubCategory")
data class SubCategory(
    @PrimaryKey(autoGenerate = true) val subCategoryId: Int = 0,
    val name: String,
    val userId: Int
)
