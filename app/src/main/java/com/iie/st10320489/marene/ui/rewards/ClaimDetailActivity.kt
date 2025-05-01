package com.iie.st10320489.marene.ui.rewards


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.iie.st10320489.marene.R

class ClaimDetailActivity : Fragment() {

    private lateinit var imageClaimDetail: ImageView
    private lateinit var confirmButton: Button
    private lateinit var cancelButton: Button

    override fun onCreateView (inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val rootView: View = inflater.inflate(R.layout.activity_claim_detail, container, false)

        // Bind the views
        imageClaimDetail = rootView.findViewById(R.id.imageClaimDetail)
        confirmButton = rootView.findViewById(R.id.btnConfirm)
        cancelButton = rootView.findViewById(R.id.btnCancel)

        // Get the data passed from the MainActivity2
        val imageResId = arguments?.getInt("IMAGE_RES_ID") ?: 0

        // Set the image on the new page
        imageClaimDetail.setImageResource(imageResId)

        // Setting the onClickListener for the buttons
        confirmButton.setOnClickListener {
            Toast.makeText(requireContext(), "Reward Claimed", Toast.LENGTH_SHORT).show()
        }


        cancelButton.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
            // Close the fragment page
        }


        ViewCompat.setOnApplyWindowInsetsListener(rootView.findViewById(R.id.mainClaim)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        return rootView
    }

}
