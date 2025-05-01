package com.iie.st10320489.marene.graphs

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.iie.st10320489.marene.R
import com.iie.st10320489.marene.data.dao.TransactionDao
import com.iie.st10320489.marene.data.dao.UserDao
import com.iie.st10320489.marene.data.database.DatabaseInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class MonthlySummaryFragment : Fragment() {

    private lateinit var transactionDao: TransactionDao
    private lateinit var userDao: UserDao

    private lateinit var totalIncomeText: TextView
    private lateinit var totalExpenseText: TextView
    private lateinit var barGraph: ProgressBar
    private lateinit var percentageText: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_monthly_summary, container, false)
        totalIncomeText = view.findViewById(R.id.value_total_balance)
        totalExpenseText = view.findViewById(R.id.value_total_expense)
        barGraph = view.findViewById(R.id.bar_graph)
        percentageText = view.findViewById(R.id.text_percentage_spent)

        val db = DatabaseInstance.getDatabase(requireContext())
        transactionDao = db.transactionDao()
        userDao = db.userDao()

        lifecycleScope.launch {
            updateMonthlySummary()

        }

        return view
    }

    private suspend fun updateMonthlySummary() {
        Log.d("MonthlySummaryFragment", "Starting updateMonthlySummary method.")
        val sharedPreferences = requireContext().getSharedPreferences("UserPreferences", Context.MODE_PRIVATE)
        val email = sharedPreferences.getString("currentUserEmail", null)
        Log.d("MonthlySummaryFragment", "Email retrieved: $email")
        Log.d("MonthlySummaryFragment", "Email retrieved: $email") // Log email value

        if (email != null) {
            val userId = userDao.getUserIdByEmail(email)
            Log.d("MonthlySummaryFragment", "User ID: $userId") // Log userId value

            if (userId != null) {
                val currentDate = LocalDate.now()
                val month = currentDate.format(DateTimeFormatter.ofPattern("MM"))
                val year = currentDate.format(DateTimeFormatter.ofPattern("yyyy"))

                Log.d("MonthlySummaryFragment", "Current month: $month, Current year: $year") // Log date

                val expenses = transactionDao.getMonthlyExpenses(userId, month, year)
                val income = transactionDao.getMonthlyIncome(userId, month, year)

                Log.d("MonthlySummaryFragment", "Expenses: $expenses, Income: $income") // Log fetched data

                val totalExpense = expenses.sumOf { it.amount }
                val totalIncome = income.sumOf { it.amount }

                Log.d("MonthlySummaryFragment", "Total Expense: $totalExpense, Total Income: $totalIncome") // Log totals

                withContext(Dispatchers.Main) {
                    totalIncomeText.text = "R %.2f".format(totalIncome)
                    totalExpenseText.text = "-R %.2f".format(totalExpense)

                    val percentageSpent = if (totalIncome == 0.0) 0 else (totalExpense / totalIncome * 100).toInt()
                    barGraph.progress = 100 - percentageSpent
                    percentageText.text = "You've spent $percentageSpent% of your income"

                    Log.d("MonthlySummaryFragment", "Percentage spent: $percentageSpent") // Log percentage spent
                }
            }
        }
    }
}
