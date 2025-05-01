package com.iie.st10320489.marene.ui.subcategory

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.iie.st10320489.marene.R
import com.iie.st10320489.marene.data.database.DatabaseInstance
import com.iie.st10320489.marene.data.entities.SubCategory
import kotlinx.coroutines.launch

class SubCategoryDialogFragment : DialogFragment() {

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        val inflater = requireActivity().layoutInflater
        val view = inflater.inflate(R.layout.dialog_add_subcategory, null)
        val editTextSubcategoryName = view.findViewById<EditText>(R.id.editTextSubcategoryName)

        sharedPreferences = requireActivity().getSharedPreferences("UserPreferences", Context.MODE_PRIVATE)

        builder.setView(view)
            .setTitle("Add Subcategory")
            .setPositiveButton("Add") { _, _ ->
                val subcategoryName = editTextSubcategoryName.text.toString().trim()
                if (subcategoryName.isNotEmpty()) {
                    addSubCategoryToDatabase(subcategoryName)
                }
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }

        return builder.create()
    }

    private fun addSubCategoryToDatabase(subcategoryName: String) {
        val db = DatabaseInstance.getDatabase(requireContext())
        val subCategoryDao = db.subCategoryDao()

        val currentUserEmail = sharedPreferences.getString("currentUserEmail", null)

        if (currentUserEmail != null) {
            lifecycleScope.launch {
                val userDao = db.userDao()
                val userId = userDao.getUserIdByEmail(currentUserEmail)

                if (userId != null) {
                    val subCategory = SubCategory(
                        name = subcategoryName,
                        userId = userId
                    )

                    subCategoryDao.insertSubCategory(subCategory)

                    // After inserting new subcategory, reload list
                    (parentFragment as? SubcategoryFragment)?.loadSubcategories()
                }
            }
        }
    }


}
