package com.iie.st10320489.marene.ui.profile

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.iie.st10320489.marene.LoginActivity
import com.iie.st10320489.marene.R
import com.iie.st10320489.marene.ui.profile.EditProfileFragment

class SettingsFragment : Fragment() {

    private var userId: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val logoutButton: RelativeLayout = view.findViewById(R.id.btnLogout)
        val editProfileRow: RelativeLayout = view.findViewById(R.id.btnEditProfile)
        val privacyRow: RelativeLayout = view.findViewById(R.id.btnPrivacy)
        val  helpRow: RelativeLayout = view.findViewById(R.id.btnHelp)

        logoutButton.setOnClickListener {
            showLogoutDialog()
        }

        editProfileRow.setOnClickListener {
            Toast.makeText(context, "Continue in Part 3", Toast.LENGTH_SHORT).show()
            //findNavController().navigate(R.id.action_settingFragment_to_edit_profile_Fragment)
        }

        privacyRow.setOnClickListener {
            Toast.makeText(context, "Continue in Part 3", Toast.LENGTH_SHORT).show()
        }

        helpRow.setOnClickListener {
            Toast.makeText(context, "Continue in Part 3", Toast.LENGTH_SHORT).show()
        }

    }


    private fun showLogoutDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Log out")
            .setMessage("Are you sure you want to log out? You'll need to login again to use the app.")
            .setPositiveButton("Log out") { _, _ ->
                performLogout()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun performLogout() {
        // Clear user session
        val sharedPreferences = requireContext().getSharedPreferences("user_prefs", android.content.Context.MODE_PRIVATE)
        sharedPreferences.edit().clear().apply()

        // Redirect to login screen
        val intent = Intent(requireContext(), LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        requireActivity().finish()
    }
}
