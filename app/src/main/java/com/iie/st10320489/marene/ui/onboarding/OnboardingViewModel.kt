package com.iie.st10320489.marene.ui.onboarding

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iie.st10320489.marene.R
import com.iie.st10320489.marene.data.dao.CategoryDao
import com.iie.st10320489.marene.data.entities.Category
import kotlinx.coroutines.launch
import com.iie.st10320489.marene.ui.onboarding.BudgetSelectionActivity

class OnboardingViewModel(
    private val categoryDao: CategoryDao
) : ViewModel() {

    // Save selected Categories
    fun saveSelectedCategories(userId: Int, selectedCategoryNames: List<String>) {
        println("Saving categories for userId: $userId")
        selectedCategoryNames.forEach { category ->
            Log.d("OnboardingViewModel", "Saving category: $category")
        }

        viewModelScope.launch {
            val categories = selectedCategoryNames.map { categoryName ->
                val (iconRes, colorRes) = mapCategoryResources(categoryName)  // Fetch resource IDs
                Category(
                    userId = userId,
                    name = categoryName,
                    icon = iconRes,  // Store the resource ID for the icon
                    colour = colorRes  // Store the resource ID for the color
                )
            }.toMutableList()

            if (!selectedCategoryNames.contains("Other")) {
                categories.add(
                    Category(
                        userId = userId,
                        name = "Other",
                        icon = R.drawable.ic_custom,  // Default icon resource ID
                        colour = R.color.red  // Default color resource ID
                    )
                )
            }

            categoryDao.insertCategories(categories)
            categories.forEach { category ->
                println("Saved category: ${category.name}")
            }
        }
    }

    // Moved here from BudgetSelectionActivity
    private fun mapCategoryResources(name: String): Pair<Int, Int> {
        return when (name) {
            "House" -> Pair(R.drawable.ic_house, R.color.rose)
            "Food" -> Pair(R.drawable.ic_food, R.color.blue)
            "Transport" -> Pair(R.drawable.ic_transport, R.color.purple)
            "Health" -> Pair(R.drawable.ic_health, R.color.orange)
            "Loans" -> Pair(R.drawable.ic_loans, R.color.tangerine)
            "Entertainment" -> Pair(R.drawable.ic_entertainment, R.color.pink)
            "Family" -> Pair(R.drawable.ic_family, R.color.yellow)
            "Custom" -> Pair(R.drawable.ic_custom, R.color.red)
            "Savings" -> Pair(R.drawable.ic_savings, R.color.teal_200)
            "Salary" -> Pair(R.drawable.ic_salary, R.color.primary)
            else -> Pair(R.drawable.ic_default, R.color.black)
        }
    }
}

