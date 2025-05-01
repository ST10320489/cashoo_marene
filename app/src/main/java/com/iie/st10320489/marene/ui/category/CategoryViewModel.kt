package com.iie.st10320489.marene.ui.category

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.iie.st10320489.marene.data.database.DatabaseInstance
import com.iie.st10320489.marene.data.entities.Category
import com.iie.st10320489.marene.data.repository.CategoryRepository
import kotlinx.coroutines.launch


class CategoryViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: CategoryRepository =
        CategoryRepository(DatabaseInstance.getDatabase(application).categoryDao())

    val allCategories by lazy { repository.getAll() }

    fun insert(category: Category) = viewModelScope.launch {
        repository.insert(category)
    }

    fun getCategoriesByUser(userId: Int) = repository.getCategoriesByUser(userId)
}
