package com.iie.st10320489.marene.ui.onboarding

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.iie.st10320489.marene.MainActivity
import com.iie.st10320489.marene.R
import com.iie.st10320489.marene.data.database.AppDatabase
import com.iie.st10320489.marene.data.database.DatabaseInstance
import com.iie.st10320489.marene.data.repository.UserSettingsRepository
import com.iie.st10320489.marene.data.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChinchillaHatActivity : AppCompatActivity() {
    private lateinit var chinchillaImage: ImageView
    private var selectedColor: String = ""
    private var selectedHat: String = ""

    private lateinit var db: AppDatabase
    private lateinit var userRepository: UserRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chinchilla_hat)

        db = DatabaseInstance.getDatabase(this)
        userRepository = UserRepository(db.userDao())

        findViewById<ImageButton>(R.id.backButton).setOnClickListener {
            finish()
        }

        chinchillaImage = findViewById(R.id.chinchillaImage)
        selectedColor = intent.getStringExtra("selectedColor") ?: "black"

        findViewById<View>(R.id.sailorButton).setOnClickListener {
            selectedHat = "sailor"
            chinchillaImage.setImageResource(getDrawableResource(selectedColor, selectedHat))
        }
        findViewById<View>(R.id.mexicanButton).setOnClickListener {
            selectedHat = "mexican"
            chinchillaImage.setImageResource(getDrawableResource(selectedColor, selectedHat))
        }
        findViewById<View>(R.id.strawberryButton).setOnClickListener {
            selectedHat = "strawberry"
            chinchillaImage.setImageResource(getDrawableResource(selectedColor, selectedHat))
        }
        findViewById<View>(R.id.pirateButton).setOnClickListener {
            selectedHat = "pirate"
            chinchillaImage.setImageResource(getDrawableResource(selectedColor, selectedHat))
        }

        findViewById<Button>(R.id.nextButton).setOnClickListener {
            val chinchillaString = "${selectedColor}_${selectedHat}"
            saveChinchillaToDatabase(chinchillaString)
            // TODO: Move to next activity if needed
        }
    }

    private fun getDrawableResource(color: String, hat: String): Int {
        return resources.getIdentifier("${color}_${hat}", "drawable", packageName)
    }

    private fun saveChinchillaToDatabase(chinchillaString: String) {
        val sharedPreferences = getSharedPreferences("UserPreferences", Context.MODE_PRIVATE)
        val currentUserEmail = sharedPreferences.getString("currentUserEmail", null)

        if (currentUserEmail != null) {
            lifecycleScope.launch(Dispatchers.IO) {
                val userId = userRepository.getUserIdByEmail(currentUserEmail)

                if (userId != null) {
                    val userSettings = db.userSettingsDao().getUserSettingsByUserId(userId)

                    if (userSettings != null) {
                        userSettings.color = selectedColor
                        userSettings.chinchilla = chinchillaString
                        db.userSettingsDao().updateUserSettings(userSettings)


                        println("UserSettings updated: color=${userSettings.color}, chinchilla=${userSettings.chinchilla}")


                        withContext(Dispatchers.Main) {
                            val intent = Intent(this@ChinchillaHatActivity, MainActivity::class.java)
                            intent.putExtra("navigateToHome", true)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                            finish()
                        }

                    }
                }
            }
        }
    }
}
