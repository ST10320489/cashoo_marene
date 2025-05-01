package com.iie.st10320489.marene.ui.analysis

import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.google.android.material.tabs.TabLayout
import com.iie.st10320489.marene.R
import com.iie.st10320489.marene.data.database.AppDatabase
import com.iie.st10320489.marene.data.database.DatabaseInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import android.util.Log
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.iie.st10320489.marene.graphs.MonthlySummaryFragment
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class AnalysisFragment : Fragment() {

    private val TAG = "AnalysisFragment"  // for log tagging


    private lateinit var pieChart: PieChart
    private lateinit var barChart: BarChart
    private lateinit var tabLayout: TabLayout
    private val viewModel: AnalysisViewModel by viewModels()

    // Add database instance and DAOs
    private lateinit var db: AppDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_analysis, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pieChart = view.findViewById(R.id.pieChart)
        barChart = view.findViewById(R.id.barChart)
        tabLayout = view.findViewById(R.id.tabLayout)

        db = DatabaseInstance.getDatabase(requireContext())
        Log.d(TAG, "Database initialized")

        setupPieChart()
        setupTabs()
        addMonthlySummaryFragment()
    }

    private fun setupPieChart() {
        val sharedPreferences = requireContext().getSharedPreferences("UserPreferences", Context.MODE_PRIVATE)
        val email = sharedPreferences.getString("currentUserEmail", null)
        Log.d(TAG, "Retrieved email from SharedPreferences: $email")

        if (email == null) {
            Toast.makeText(context, "No user logged in", Toast.LENGTH_SHORT).show()
            Log.e(TAG, "No email found in SharedPreferences")
            return
        }

        lifecycleScope.launch {
            val userId = withContext(Dispatchers.IO) {
                db.userDao().getUserIdByEmail(email).also {
                    Log.d(TAG, "User ID fetched for $email: $it")
                }
            }

            if (userId == null) {
                Log.e(TAG, "User ID not found for email: $email")
                return@launch
            }

            val transactionsWithCategory = withContext(Dispatchers.IO) {
                db.transactionDao().getTransactionsWithCategory(userId).also {
                    Log.d(TAG, "Fetched ${it.size} transactions for userId: $userId")
                }
            }

            if (transactionsWithCategory.isEmpty()) {
                Toast.makeText(context, "No expense data", Toast.LENGTH_SHORT).show()
                Log.w(TAG, "No transactions with category found")
                return@launch
            }

            val totalExpenses = transactionsWithCategory.sumOf { it.transaction.amount }
            Log.d(TAG, "Total expenses: $totalExpenses")

            val categorySums = transactionsWithCategory.groupBy { it.category }
                .mapValues { entry -> entry.value.sumOf { it.transaction.amount } }

            categorySums.forEach { (category, amount) ->
                Log.d(TAG, "Category: ${category.name}, Amount: $amount")
            }

            val entries = categorySums.map { (category, amount) ->
                val percentage = (amount / totalExpenses * 100).toFloat()
                Log.d(TAG, "PieEntry -> Category: ${category.name}, Percentage: $percentage")
                PieEntry(percentage, category.name)
            }

            val context = requireContext() // Get the context

            val colors = categorySums.keys.map {
                try {
                    // Assuming category.colour is a color resource ID, we use ContextCompat to get the actual color
                    ContextCompat.getColor(context, it.colour) // Retrieve the color from resource ID
                } catch (e: Resources.NotFoundException) {
                    Log.w(TAG, "Color resource not found for ID '${it.colour}', using default")
                    Color.GRAY // Fallback to a default color
                }
            }


            val dataSet = PieDataSet(entries, "").apply {
                this.colors = colors
                valueTextColor = Color.TRANSPARENT
            }

            val data = PieData(dataSet)

            pieChart.data = data
            pieChart.description.isEnabled = false
            pieChart.legend.isEnabled = false
            pieChart.setDrawEntryLabels(false)
            pieChart.setHoleColor(Color.WHITE)
            pieChart.setDrawCenterText(false)
            pieChart.animateY(1000)
            pieChart.invalidate()

            val detailsLayout = view?.findViewById<LinearLayout>(R.id.detailsLayout)

// Remove old category rows (everything after header)
            if (detailsLayout != null && detailsLayout.childCount > 1) {
                detailsLayout.removeViews(1, detailsLayout.childCount - 1)
            }

// Add new rows
            categorySums.forEach { (category, amount) ->
                val percentage = (amount / totalExpenses * 100)
                val color = try {
                    ContextCompat.getColor(requireContext(), category.colour)
                } catch (e: Resources.NotFoundException) {
                    Color.GRAY
                }

                val row = LinearLayout(requireContext()).apply {
                    layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    ).apply {
                        setMargins(0, 0, 0, 8) // optional spacing between rows
                    }
                    orientation = LinearLayout.HORIZONTAL
                }

                val dotAndCategory = TextView(requireContext()).apply {
                    layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
                    text = "● ${category.name}"
                    setTextColor(color)
                    textSize = 14f
                }

                val amountView = TextView(requireContext()).apply {
                    layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
                    text = "R %.2f".format(amount)
                    setTextColor(Color.parseColor("#1C1C1E"))
                    textSize = 14f
                }

                val percentView = TextView(requireContext()).apply {
                    layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
                    text = "%.1f%%".format(percentage)
                    setTextColor(Color.parseColor("#1C1C1E"))
                    setTypeface(null, Typeface.BOLD)
                    textSize = 14f
                    gravity = Gravity.END
                }

                row.addView(dotAndCategory)
                row.addView(amountView)
                row.addView(percentView)

                detailsLayout?.addView(row)
            }


            Log.d(TAG, "Pie chart updated successfully")
        }
    }

    private fun setupTabs() {
        tabLayout.addTab(tabLayout.newTab().setText("Weekly"))
        tabLayout.addTab(tabLayout.newTab().setText("Monthly"), true)
        tabLayout.addTab(tabLayout.newTab().setText("Yearly"))

        Log.d(TAG, "Tabs initialized")

        setChartData("Monthly")

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                Log.d(TAG, "Tab selected: ${tab.text}")
                setChartData(tab.text.toString())
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }

    private fun addMonthlySummaryFragment() {
        // Check if the fragment already exists
        val fragmentManager = childFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        // Create an instance of MonthlySummaryFragment
        val fragment = MonthlySummaryFragment()

        // Add or replace the fragment in the container
        fragmentTransaction.replace(R.id.fragment_container, fragment)
        fragmentTransaction.addToBackStack(null)  // Optionally add the transaction to the back stack
        fragmentTransaction.commit()
    }

    private fun setChartData(mode: String) {
        val sharedPreferences = requireContext().getSharedPreferences("UserPreferences", Context.MODE_PRIVATE)
        val email = sharedPreferences.getString("currentUserEmail", null) ?: return

        lifecycleScope.launch {
            val userId = withContext(Dispatchers.IO) {
                db.userDao().getUserIdByEmail(email)
            } ?: return@launch

            val transactions = withContext(Dispatchers.IO) {
                db.transactionDao().getTransactionsByUser(userId) // You'll need to create this DAO method
            }

            val incomeEntries = ArrayList<BarEntry>()
            val expenseEntries = ArrayList<BarEntry>()
            val labels = ArrayList<String>()

            val now = java.time.LocalDate.now()
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
            //val txDate = LocalDateTime.parse(it.transaction.dateTime, formatter).toLocalDate()


            when (mode) {
                "Weekly" -> {
                    val weekStart = now.with(java.time.DayOfWeek.MONDAY)
                    for (i in 0..6) {
                        val day = weekStart.plusDays(i.toLong())
                        val dailyTrans = transactions.filter {
                            java.time.LocalDate.parse(it.transaction.dateTime, formatter) == day
                        }
                        val income = dailyTrans.filter { it.transaction.expense == false }.sumOf { it.transaction.amount }.toFloat()
                        val expense = dailyTrans.filter { it.transaction.expense == true }.sumOf { it.transaction.amount }.toFloat()
                        incomeEntries.add(BarEntry(i.toFloat(), income))
                        expenseEntries.add(BarEntry(i.toFloat(), expense))
                        labels.add(day.dayOfWeek.name.take(3)) // e.g., MON
                    }
                }

                "Monthly" -> {
                    val monthStart = now.withDayOfMonth(1)
                    val weeks = listOf(0, 7, 14, 21)
                    for ((i, offset) in weeks.withIndex()) {
                        val weekStart = monthStart.plusDays(offset.toLong())
                        val weekEnd = weekStart.plusDays(6)
                        val weeklyTrans = transactions.filter {
                            val txDate = java.time.LocalDate.parse(it.transaction.dateTime, formatter)
                            txDate in weekStart..weekEnd
                        }
                        val income = weeklyTrans.filter { it.transaction.expense == false }.sumOf { it.transaction.amount }.toFloat()
                        val expense = weeklyTrans.filter { it.transaction.expense == true }.sumOf { it.transaction.amount }.toFloat()
                        incomeEntries.add(BarEntry(i.toFloat(), income))
                        expenseEntries.add(BarEntry(i.toFloat(), expense))
                        labels.add("W${i + 1}")
                    }
                }

                "Yearly" -> {
                    for (i in 1..12) {
                        val monthlyTrans = transactions.filter {
                            val txDate = java.time.LocalDate.parse(it.transaction.dateTime, formatter)
                            txDate.monthValue == i && txDate.year == now.year
                        }
                        val income = monthlyTrans.filter { it.transaction.expense == false }.sumOf { it.transaction.amount }.toFloat()
                        val expense = monthlyTrans.filter { it.transaction.expense == true }.sumOf { it.transaction.amount }.toFloat()
                        incomeEntries.add(BarEntry((i - 1).toFloat(), income))
                        expenseEntries.add(BarEntry((i - 1).toFloat(), expense))
                        labels.add(java.time.Month.of(i).name.take(3)) // e.g., JAN
                    }
                }
            }

            val incomeDataSet = BarDataSet(incomeEntries, "Income").apply {
                color = ContextCompat.getColor(requireContext(), R.color.income)
            }

            val expenseDataSet = BarDataSet(expenseEntries, "Expenses").apply {
                color = ContextCompat.getColor(requireContext(), R.color.outcome)
            }

            val barData = BarData(incomeDataSet, expenseDataSet).apply {
                barWidth = 0.4f
                groupBars(0f, 0.2f, 0f)
            }

            barChart.apply {
                data = barData
                xAxis.apply {
                    valueFormatter = IndexAxisValueFormatter(labels)
                    granularity = 1f
                    setCenterAxisLabels(true)
                    axisMinimum = 0f
                    axisMaximum = labels.size.toFloat()
                    position = com.github.mikephil.charting.components.XAxis.XAxisPosition.BOTTOM
                }
                axisLeft.axisMinimum = 0f
                axisRight.isEnabled = false
                description.isEnabled = false
                legend.isEnabled = true
                setVisibleXRangeMaximum(labels.size.toFloat())
                invalidate()
            }
        }
    }

}
