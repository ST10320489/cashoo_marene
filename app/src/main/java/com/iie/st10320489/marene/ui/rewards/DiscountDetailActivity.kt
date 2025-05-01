package com.iie.st10320489.marene.ui.rewards

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.iie.st10320489.marene.R

class DiscountDetailActivity : Fragment() {

    override fun onCreateView (inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val rootView: View = inflater.inflate(R.layout.activity_discount_detail, container, false)

        // Retrieve views from layout
        val detailImg: ImageView = rootView.findViewById(R.id.detailImg)
        val btnConfirm: Button = rootView.findViewById(R.id.btnConfirm)
        val btnCancel: Button = rootView.findViewById(R.id.btnCancel)

        // Get the discount image resource from the intent
        val imageResId = arguments?.getInt("discount_image", -1) ?: -1
        if (imageResId != -1) {
            detailImg.setImageResource(imageResId)
        }

        // Set up the button click events
        btnConfirm.setOnClickListener {
            Toast.makeText(requireContext(), "Discount Confirmed", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.navigation_rewards_qr)

            // Here you can add further actions on confirmation
        }

        btnCancel.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack() // Simply go back to the previous screen
        }


        ViewCompat.setOnApplyWindowInsetsListener(rootView.findViewById(R.id.discMain)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        return rootView
    }
}
