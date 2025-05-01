package com.iie.st10320489.marene.ui.onboarding

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.iie.st10320489.marene.R
import com.iie.st10320489.marene.databinding.ActivityOnboarding2Binding
import com.iie.st10320489.marene.ui.onboarding.BudgetSelectionActivity

class OnboardingActivity2 : AppCompatActivity() {

    private lateinit var binding: ActivityOnboarding2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityOnboarding2Binding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.nextButton.setOnClickListener {

            val intent = Intent(this, BudgetSelectionActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
