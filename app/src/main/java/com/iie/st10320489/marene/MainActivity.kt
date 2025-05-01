package com.iie.st10320489.marene

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import android.os.Build
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.content.ContextCompat
import android.util.TypedValue
import android.widget.ProgressBar
import com.iie.st10320489.marene.ui.subcategory.SubCategoryDialogFragment
import com.iie.st10320489.marene.ui.subcategory.SubcategoryFragment


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        //change color to transparent
        // Change status bar color
        window.statusBarColor = ContextCompat.getColor(this, R.color.primary)

        // Make it transparent (Optional)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = android.graphics.Color.TRANSPARENT
        }


        val navController: NavController = findNavController(R.id.nav_host_fragment_activity_main)

        val shouldNavigateToHome = intent.getBooleanExtra("navigateToHome", false)
        if (shouldNavigateToHome) {
            navController.navigate(R.id.navigation_home)
        }

        val bottomNavView: BottomNavigationView = findViewById(R.id.nav_view)
        val topNav = findViewById<androidx.appcompat.widget.Toolbar>(R.id.top_nav)
        val backButton = findViewById<ImageButton>(R.id.topNavBackButton)
        val title = findViewById<TextView>(R.id.topNavTitle)
        val rightButton = findViewById<ImageButton>(R.id.topNavRightButton)


        bottomNavView.setupWithNavController(navController)


        // Listen for navigation changes
        navController.addOnDestinationChangedListener { _, destination, arguments ->
            when (destination.id) {
                R.id.navigation_home -> {
                    topNav.visibility = View.GONE
                }
                R.id.settingsFragment -> {
                    topNav.visibility = View.VISIBLE
                    backButton.visibility = View.VISIBLE
                    title.text = "Settings"
                    rightButton.visibility = View.GONE
                }
                R.id.editProfileFragment -> {
                    topNav.visibility = View.VISIBLE
                    backButton.visibility = View.VISIBLE
                    title.text = "Edit Profile"
                    rightButton.visibility = View.GONE
                }
                R.id.navigation_analysis -> {
                    topNav.visibility = View.VISIBLE
                    backButton.visibility = View.GONE
                    title.text = "Analysis"
                    rightButton.visibility = View.GONE
                    rightButton.setImageResource(R.drawable.ic_search)
                }
                R.id.navigation_add -> {
                    topNav.visibility = View.VISIBLE
                    backButton.visibility = View.GONE
                    title.text = "Add Entry"
                    rightButton.visibility = View.GONE
                    rightButton.setImageResource(R.drawable.ic_search)
                }
                R.id.navigation_category -> {
                    topNav.visibility = View.VISIBLE
                    backButton.visibility = View.GONE
                    title.text = "Categories"
                    rightButton.visibility = View.GONE
                    rightButton.setImageResource(R.drawable.ic_search)
                }
                R.id.transactionFragment -> {
                    topNav.visibility = View.VISIBLE
                    backButton.visibility = View.VISIBLE // Show back button
                    title.text = arguments?.getString("categoryName") ?: "Transactions"
                    rightButton.visibility = View.VISIBLE
                    rightButton.setImageResource(R.drawable.ic_search)
                }
                R.id.filterFragment -> {
                    topNav.visibility = View.VISIBLE
                    backButton.visibility = View.VISIBLE
                    title.text = arguments?.getString("categoryName") ?: "Filter"
                    rightButton.visibility = View.GONE
                }

                R.id.subcategoryFragment -> {
                    topNav.visibility = View.VISIBLE
                    backButton.visibility = View.VISIBLE
                    title.text = arguments?.getString("categoryName") ?: "Subcategories"
                    rightButton.visibility = View.VISIBLE
                    rightButton.setImageResource(R.drawable.ic_add)

                    // Set onClick listener for the "add" button in SubcategoryFragment
                    rightButton.setOnClickListener {
                        if (navController.currentDestination?.id == R.id.subcategoryFragment) {
                            // Launch the dialog to add a subcategory
                            showAddSubcategoryDialog()
                        }
                    }
                }

                R.id.transactionDetailsFragment -> {
                    topNav.visibility = View.VISIBLE
                    backButton.visibility = View.VISIBLE
                    rightButton.visibility = View.GONE
                    rightButton.setImageResource(R.drawable.ic_edit)
                }

                else -> {
                    topNav.visibility = View.VISIBLE
                    backButton.visibility = View.GONE
                    title.text = "Rewards"
                    rightButton.visibility = View.GONE
                }
            }
        }

        // Back button functionality
        backButton.setOnClickListener {
            navController.navigateUp()
        }

    }

    // Show the "Add Subcategory" dialog, but now in the fragment context
    private fun showAddSubcategoryDialog() {
        val dialogFragment = SubCategoryDialogFragment()
        dialogFragment.show(supportFragmentManager, "AddSubcategoryDialog")
    }
}
