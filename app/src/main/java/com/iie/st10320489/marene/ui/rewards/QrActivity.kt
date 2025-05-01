package com.iie.st10320489.marene.ui.rewards

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.iie.st10320489.marene.R

class QrFragment : Fragment() {

    override fun onCreateView (inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val rootView: View = inflater.inflate(R.layout.activity_qr, container, false)

        val btnContinue: Button = rootView.findViewById(R.id.btnContinue)


        btnContinue.setOnClickListener {
            Toast.makeText(requireContext(), "Reward claimed", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.qrFragment)

        }


        ViewCompat.setOnApplyWindowInsetsListener(rootView.findViewById(R.id.qrMain)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        return rootView
    }
}