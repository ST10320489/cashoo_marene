package com.iie.st10320489.marene.ui.rewards

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.iie.st10320489.marene.R
import com.iie.st10320489.marene.data.database.AppDatabase
import com.iie.st10320489.marene.data.database.DatabaseInstance
import com.iie.st10320489.marene.databinding.FragmentRewardsBinding
import com.iie.st10320489.marene.ui.rewards.RewardsViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class RewardsFragment : Fragment() {

    private var _binding: FragmentRewardsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    //ADAPTER
    private lateinit var bronClmAdapter: ClaimsAdapter
    private lateinit var silClmAdapter: ClaimsAdapter
    private lateinit var gldClmAdapter: ClaimsAdapter

    private lateinit var rewardsViewModel: RewardsViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        val homeViewModel =
            ViewModelProvider(this).get(RewardsViewModel::class.java)

        _binding = FragmentRewardsBinding.inflate(inflater, container, false)
        val root: View = binding.root


        // RecyclerViews setup
        binding.recyclerClmBronze.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerClmSilver.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerClmGold.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        // Sample Rewards
        val bronzeClaims = listOf(
            ClaimItem("Croissant", "5 pts", R.drawable.croissants),
            ClaimItem("Cappuccino", "8 pts", R.drawable.capuccino_jpg),
            ClaimItem("Choco Cookie", "10 pts", R.drawable.cookie),
            ClaimItem("Frappe", "12 pts", R.drawable.frappe)
        )

        val silverClaims = listOf(
            ClaimItem("Sandwich", "15 pts", R.drawable.grilled),
            ClaimItem("Graduation", "18 pts", R.drawable.kanye),
            ClaimItem("Journal", "20 pts", R.drawable.journal),
            ClaimItem("Energy Pack", "22 pts", R.drawable.energy)
        )

        val goldClaims = listOf(
            ClaimItem("Shoe Cleaning", "25 pts", R.drawable.clean),
            ClaimItem("Cashoo Bag", "40 pts", R.drawable.kanbag),
            ClaimItem("Two Burritos", "35 pts", R.drawable.burrito),
            ClaimItem("Jordan 4s", "50 pts", R.drawable.jordan4)
        )

        // Adapters
        bronClmAdapter = ClaimsAdapter(bronzeClaims)
        silClmAdapter = ClaimsAdapter(silverClaims)
        gldClmAdapter = ClaimsAdapter(goldClaims)

        binding.recyclerClmBronze.adapter = bronClmAdapter
        binding.recyclerClmSilver.adapter = silClmAdapter
        binding.recyclerClmGold.adapter = gldClmAdapter

        val sharedPreferences = requireContext().getSharedPreferences("UserPreferences", Context.MODE_PRIVATE)

        // Set the Button listeners
        binding.ItemClaim.setOnClickListener {
            // Check if the user has already claimed rewards this month
            val currentMonth = SimpleDateFormat("MM", Locale.getDefault()).format(Date())
            val currentYear = SimpleDateFormat("yyyy", Locale.getDefault()).format(Date())

            lifecycleScope.launch {
                val currentUserEmail = sharedPreferences.getString("currentUserEmail", null)

                if (currentUserEmail != null) {
                    val db = DatabaseInstance.getDatabase(requireContext().applicationContext)
                    val userDao = db.userDao()
                    val transactionDao = db.transactionDao()
                    val userSettingsDao = db.userSettingsDao()

                    val user = withContext(Dispatchers.IO) {
                        userDao.findUserByEmail(currentUserEmail)
                    }

                    user?.let {
                        // Get the current user's total expenses and goals
                        val userSettings = withContext(Dispatchers.IO) {
                            userSettingsDao.getUserSettingsByUserId(it.userId)
                        }

                        val expensesTransactions = withContext(Dispatchers.IO) {
                            transactionDao.getMonthlyExpenses(it.userId, currentMonth, currentYear)
                        }

                        val totalExpenses = expensesTransactions.sumOf { txn -> txn.amount }
                        val maxGoal = userSettings?.maxGoal ?: 0.0

                        val expensePercent = if (maxGoal > 0) (totalExpenses / maxGoal * 100).coerceAtMost(100.0).toInt() else 0

                        val savingsTransactions = withContext(Dispatchers.IO) {
                            transactionDao.getMonthlySavings(it.userId, currentMonth, currentYear)
                        }

                        val totalSaved = savingsTransactions.sumOf { txn -> txn.amount }
                        val minGoal = userSettings?.minGoal ?: 0.0

                        val minPercent = if (minGoal > 0) (totalSaved / minGoal * 100).coerceAtMost(100.0).toInt() else 0

                        // Check conditions and apply the logic
                        if (expensePercent < 100 && minPercent == 100) {
                            // Update cashoos with 20 if both conditions are met
                            val newCashoos = it.cashoos + 20
                            it.cashoos = newCashoos

                            // Update the cashoos in the database
                            withContext(Dispatchers.IO) {
                                userDao.updateUser(it)
                            }

                            // Display a toast message
                            Toast.makeText(requireContext(), "You earned 20 Cashoos!", Toast.LENGTH_SHORT).show()
                        } else if (expensePercent < 100) {
                            // Only increase cashoos by 10 if the expense condition is met
                            val newCashoos = it.cashoos + 10
                            it.cashoos = newCashoos

                            // Update the cashoos in the database
                            withContext(Dispatchers.IO) {
                                userDao.updateUser(it)
                            }

                            Toast.makeText(requireContext(), "You earned 10 Cashoos!", Toast.LENGTH_SHORT).show()
                        } else if (minPercent == 100) {
                            // Only increase cashoos by 5 if the savings goal condition is met
                            val newCashoos = it.cashoos + 5
                            it.cashoos = newCashoos

                            // Update the cashoos in the database
                            withContext(Dispatchers.IO) {
                                userDao.updateUser(it)
                            }

                            Toast.makeText(requireContext(), "You earned 5 Cashoos!", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(requireContext(), "You don't qualify for rewards this month.", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }


        /*binding.helpButton.setOnClickListener {
            findNavController().navigate(R.id.navigation_rewards_help)
        }*/

        binding.discPage.setOnClickListener {
            findNavController().navigate(R.id.navigation_rewards_discount)
        }

        // LOAD USER POINTS FROM DATABASE
        //val sharedPreferences = requireContext().getSharedPreferences("UserPreferences", Context.MODE_PRIVATE)
        val currentUserEmail = sharedPreferences.getString("currentUserEmail", null)

        if (currentUserEmail != null) {
            val db = DatabaseInstance.getDatabase(requireContext().applicationContext)
            val userDao = db.userDao()
            val transactionDao = db.transactionDao()
            val userSettingsDao = db.userSettingsDao()

            lifecycleScope.launch {
                val currentUserEmail = sharedPreferences.getString("currentUserEmail", null)

                if (currentUserEmail != null) {
                    val db = DatabaseInstance.getDatabase(requireContext().applicationContext)
                    val userDao = db.userDao()

                    val user = withContext(Dispatchers.IO) {
                        // Log the email to check if it's being fetched correctly
                        Log.d("UserEmail", "Fetching user with email: $currentUserEmail")
                        userDao.findUserByEmail(currentUserEmail)
                    }

                    // Check if the user is found
                    user?.let {
                        // User is found, proceed with your logic
                        Log.d("UserInfo", "User found: ${it.userId}, ${it.cashoos}")
                        val formattedCash = "C ${String.format("%.2f", it.cashoos)}"
                        view?.findViewById<TextView>(R.id.txtPoints2)?.text = formattedCash

                        // Get current month and year
                        val currentMonth = SimpleDateFormat("MM", Locale.getDefault()).format(Date())
                        val currentYear = SimpleDateFormat("yyyy", Locale.getDefault()).format(Date())

                        // Fetch user settings and transactions
                        val userSettings = withContext(Dispatchers.IO) {
                            userSettingsDao.getUserSettingsByUserId(it.userId)
                        }

                        // Fetch monthly savings
                        val savingsTransactions = withContext(Dispatchers.IO) {
                            transactionDao.getMonthlySavings(it.userId, currentMonth, currentYear)
                        }

                        val totalSaved = savingsTransactions.sumOf { txn -> txn.amount }
                        val minGoal = userSettings?.minGoal ?: 0.0
                        val maxGoal = userSettings?.maxGoal ?: 0.0

                        val minPercent = if (minGoal > 0) (totalSaved / minGoal * 100).coerceAtMost(100.0).toInt() else 0

                        view?.findViewById<TextView>(R.id.minGoalPercentage)?.text = "$minPercent%"

                        // Fetch monthly expenses
                        val expensesTransactions = withContext(Dispatchers.IO) {
                            transactionDao.getMonthlyExpenses(it.userId, currentMonth, currentYear)
                        }

                        val totalExpenses = expensesTransactions.sumOf { txn -> txn.amount }

                        // Calculate expense percentage
                        val expensePercent = if (maxGoal > 0) (totalExpenses / maxGoal * 100).coerceAtMost(100.0).toInt() else 0

                        view?.findViewById<TextView>(R.id.maxGoalPercentage)?.text = "$expensePercent%"
                    } ?: run {
                        // Handle case where user is not found
                        Log.e("UserInfo", "User not found for email: $currentUserEmail")
                    }
                }
            }


        }


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}