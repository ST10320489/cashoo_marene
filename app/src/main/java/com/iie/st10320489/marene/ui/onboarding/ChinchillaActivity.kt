package com.iie.st10320489.marene.ui.onboarding

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.iie.st10320489.marene.R

class ChinchillaActivity : AppCompatActivity(){

    private lateinit var chinchillaImage: ImageView
    private var selectedColor: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chinchilla)


        findViewById<ImageButton>(R.id.backButton).setOnClickListener {
            finish()
        }

        chinchillaImage = findViewById(R.id.chinchillaImage)


        findViewById<View>(R.id.whiteButton).setOnClickListener {
            selectedColor = "white"
            chinchillaImage.setImageResource(R.drawable.white_nohat)
        }
        findViewById<View>(R.id.beigeButton).setOnClickListener {
            selectedColor = "beige"
            chinchillaImage.setImageResource(R.drawable.beige_nohat)
        }
        findViewById<View>(R.id.violetButton).setOnClickListener {
            selectedColor = "voilet"
            chinchillaImage.setImageResource(R.drawable.voilet_nohat)
        }
        findViewById<View>(R.id.brownButton).setOnClickListener {
            selectedColor = "brown"
            chinchillaImage.setImageResource(R.drawable.brown_nohat)
        }
        findViewById<View>(R.id.blackButton).setOnClickListener {
            selectedColor = "black"
            chinchillaImage.setImageResource(R.drawable.black_nohat)
        }
        findViewById<View>(R.id.greyButton).setOnClickListener {
            selectedColor = "grey"
            chinchillaImage.setImageResource(R.drawable.grey_nohat)
        }


        findViewById<Button>(R.id.nextButton).setOnClickListener {
            val intent = Intent(this, ChinchillaHatActivity::class.java)
            intent.putExtra("selectedColor", selectedColor)  // Pass color to next activity
            startActivity(intent)
            finish()
        }
    }

}