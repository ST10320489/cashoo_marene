package com.iie.st10320489.marene.ui.onboarding

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.iie.st10320489.marene.R
import com.iie.st10320489.marene.databinding.ActivityOnboardingBinding
import com.iie.st10320489.marene.ui.onboarding.OnboardingActivity2

class OnboardingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOnboardingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.nextButton.setOnClickListener {

            val intent = Intent(this, OnboardingActivity2::class.java)
            startActivity(intent)
            finish()
        }
    }
}
