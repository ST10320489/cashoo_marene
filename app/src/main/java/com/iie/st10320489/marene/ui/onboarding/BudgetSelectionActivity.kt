package com.iie.st10320489.marene.ui.onboarding

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.iie.st10320489.marene.R
import com.iie.st10320489.marene.data.database.AppDatabase
import com.iie.st10320489.marene.data.database.DatabaseInstance
import com.iie.st10320489.marene.data.repository.UserRepository
import com.iie.st10320489.marene.databinding.ActivityBudgetSelectionBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BudgetSelectionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBudgetSelectionBinding
    private val selectedCategories = mutableListOf<String>()
    private val categories = listOf(
        "House", "Food", "Transport", "Health", "Loans",
        "Entertainment", "Family", "Savings", "Salary"
    )


    private val onboardingViewModel: OnboardingViewModel by viewModels {
        OnboardingViewModelFactory(
            categoryDao = DatabaseInstance.getDatabase(application).categoryDao()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBudgetSelectionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupCategoryViews()
        setupButtons()
    }

    private fun setupCategoryViews() {
        categories.forEach { category ->
            val categoryView = TextView(this).apply {
                text = category
                textSize = 16f
                setTextColor(ContextCompat.getColor(context, android.R.color.black))
                setPadding(32, 32, 32, 32)
                background = ContextCompat.getDrawable(context, R.drawable.category_unselected)

                layoutParams = LinearLayout.LayoutParams(
                    resources.getDimensionPixelSize(R.dimen.category_width),
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    bottomMargin = 16
                }

                setOnClickListener {
                    if (selectedCategories.contains(category)) {
                        selectedCategories.remove(category)
                        background = ContextCompat.getDrawable(context, R.drawable.category_unselected)
                    } else {
                        selectedCategories.add(category)
                        background = ContextCompat.getDrawable(context, R.drawable.category_selected)
                    }
                }
            }
            binding.categoriesContainer.addView(categoryView)
        }
    }

    private fun setupButtons() {
        binding.backButton.setOnClickListener {
            finish()
        }

        binding.nextButton.setOnClickListener {
            if (selectedCategories.isNotEmpty()) {

                saveUserSelectedCategories()
            } else {
                Toast.makeText(this, "Please select at least one category", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun getCurrentUserEmail(): String {
        val sharedPreferences = getSharedPreferences("UserPreferences", MODE_PRIVATE)
        val email = sharedPreferences.getString("currentUserEmail", "")
        Log.d("BudgetSelectionActivity", "Retrieved email: $email")
        return email ?: ""
    }



    private fun saveUserSelectedCategories() {


        val userDao = DatabaseInstance.getDatabase(application).userDao()  // Correct usage
        val userRepository = UserRepository(userDao)
        // Fetch the current user email (assuming you have it saved in SharedPreferences)
        val currentUserEmail = getCurrentUserEmail()

        // Launch a coroutine to fetch the userId asynchronously
        CoroutineScope(Dispatchers.IO).launch {
            val userId = userRepository.getUserIdByEmail(currentUserEmail)  // Suspend function call

            // After getting the userId, save selected categories
            if (userId == null) {
                // Handle the case where the user is not found
                println("User not found for email: $currentUserEmail")
            } else {
                // Proceed with saving the categories
                onboardingViewModel.saveSelectedCategories(userId, selectedCategories)
            }

            // Once the data is saved, navigate to the next screen (ensure this is done on the main thread)
            withContext(Dispatchers.Main) {
                val intent = Intent(this@BudgetSelectionActivity, SavingsGoalActivity::class.java)
                startActivity(intent)
            }
        }
    }

}
