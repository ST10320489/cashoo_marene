package com.iie.st10320489.marene.ui.onboarding

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.iie.st10320489.marene.R
import com.iie.st10320489.marene.data.database.DatabaseInstance
import com.iie.st10320489.marene.data.entities.UserSettings
import com.iie.st10320489.marene.data.repository.UserRepository
import kotlinx.coroutines.*

class SavingsGoalActivity : AppCompatActivity() {

    private lateinit var spinner: Spinner
    private lateinit var salarySlider: SeekBar
    private lateinit var minSavingsSlider: SeekBar
    private lateinit var maxSpendingSlider: SeekBar
    private lateinit var salaryValue: TextView
    private lateinit var minSavingsValue: TextView
    private lateinit var maxSpendingValue: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_savings_goal)


        findViewById<ImageButton>(R.id.backButton).setOnClickListener {
            finish()
        }

        spinner = findViewById(R.id.paydaySpinner)
        val items = arrayOf("Select your payday", "Weekly", "Bi-weekly", "Monthly")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, items)
        spinner.adapter = adapter


        salarySlider = findViewById(R.id.salarySlider)
        minSavingsSlider = findViewById(R.id.minSavingsSlider)
        maxSpendingSlider = findViewById(R.id.maxSpendingSlider)

        salaryValue = findViewById(R.id.salaryValue)
        minSavingsValue = findViewById(R.id.minSavingsValue)
        maxSpendingValue = findViewById(R.id.maxSpendingValue)

        salarySlider.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                salaryValue.text = "R" + progress.toString() // Display progress value dynamically with R$
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        minSavingsSlider.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                minSavingsValue.text = "R" + progress.toString() // Display progress value dynamically with R$
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        maxSpendingSlider.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                maxSpendingValue.text = "R" + progress.toString() // Display progress value dynamically with R$
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })


        minSavingsSlider.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                minSavingsValue.text = "R$progress"
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        maxSpendingSlider.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                maxSpendingValue.text = "R$progress"
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })


        findViewById<Button>(R.id.nextButton).setOnClickListener {
            saveUserSettings()
        }
    }

    private fun getCurrentUserEmail(): String {
        val sharedPreferences = getSharedPreferences("UserPreferences", MODE_PRIVATE)
        return sharedPreferences.getString("currentUserEmail", "") ?: ""
    }

    private fun saveUserSettings() {
        val userDao = DatabaseInstance.getDatabase(application).userDao()
        val userSettingsDao = DatabaseInstance.getDatabase(application).userSettingsDao()
        val userRepository = UserRepository(userDao)

        val selectedPayday = spinner.selectedItem.toString()
        val salary = salarySlider.progress.toDouble()
        val minGoal = minSavingsSlider.progress.toDouble()
        val maxGoal = maxSpendingSlider.progress.toDouble()

        val currentUserEmail = getCurrentUserEmail()

        CoroutineScope(Dispatchers.IO).launch {
            val userId = userRepository.getUserIdByEmail(currentUserEmail)

            if (userId != null) {
                val userSettings = UserSettings(
                    userId = userId,
                    payday = selectedPayday,
                    salary = salary,
                    minGoal = minGoal,
                    maxGoal = maxGoal,
                    color = "",        // to be filled later
                    chinchilla = ""    // to be filled later
                )

                // Print userSettings for debugging
                println("UserSettings created: $userSettings")
                val userId = userRepository.getUserIdByEmail(currentUserEmail)
                println("Fetched userId: $userId")


                userSettingsDao.insertUserSettings(userSettings)

                withContext(Dispatchers.Main) {
                    Toast.makeText(this@SavingsGoalActivity, "Settings saved", Toast.LENGTH_SHORT).show()

                    // Move to the next screen
                    val intent = Intent(this@SavingsGoalActivity, ChinchillaActivity::class.java)
                    startActivity(intent)
                }
            } else {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@SavingsGoalActivity, "Error: User not found.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
