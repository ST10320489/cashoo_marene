package com.iie.st10320489.marene.ui.add

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.iie.st10320489.marene.data.database.AppDatabase
import com.iie.st10320489.marene.data.database.DatabaseInstance
import com.iie.st10320489.marene.data.entities.Transaction
import com.iie.st10320489.marene.databinding.FragmentAddBinding
import kotlinx.coroutines.launch
import java.util.Calendar

class AddFragment : Fragment() {

    private var userId: Int = 0
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var database: AppDatabase

    private var selectedCategoryId: Int = 0
    private var selectedSubCategoryId: Int = 0

    private var _binding: FragmentAddBinding? = null
    private val binding get() = _binding!!

    private val REQUEST_CODE_PICK_IMAGE = 100
    private var selectedImagePath: String? = null

    private var selectedDate: String = ""
    private var selectedTime: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        database = DatabaseInstance.getDatabase(requireContext())
        sharedPreferences = requireActivity().getSharedPreferences("UserPreferences", Context.MODE_PRIVATE)

        val spinner = binding.categorySpinner
        val currentUserEmail = sharedPreferences.getString("currentUserEmail", null)

        if (currentUserEmail != null) {
            lifecycleScope.launch {
                val userDao = database.userDao()
                val categoryDao = database.categoryDao()
                val subCategoryDao = database.subCategoryDao()

                val fetchedUserId = userDao.getUserIdByEmail(currentUserEmail)

                if (fetchedUserId != null) {
                    userId = fetchedUserId

                    val categories = categoryDao.getCategoriesForUser(fetchedUserId)
                    val subCategories = subCategoryDao.getSubCategoriesForUser(fetchedUserId)

                    val categoryNames = categories.map { it.name }
                    val subCategoryNames = subCategories.map { it.name }
                    val combinedNames = categoryNames + listOf("Other") + subCategoryNames
                    val combinedItems = categoryNames.map { it to "category" } +
                            listOf("Other" to "category") +
                            subCategoryNames.map { it to "subcategory" }

                    val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, combinedNames)
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spinner.adapter = adapter

                    spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                            val selectedItem = combinedItems[position]

                            if (selectedItem.second == "category") {
                                val category = categories.first { it.name == selectedItem.first }
                                selectedCategoryId = category.categoryId
                                selectedSubCategoryId = 0
                            } else if (selectedItem.second == "subcategory") {
                                val subCategory = subCategories.first { it.name == selectedItem.first }
                                selectedSubCategoryId = subCategory.subCategoryId
                                val otherCategory = categories.first { it.name == "Other" }
                                selectedCategoryId = otherCategory.categoryId
                            }
                        }

                        override fun onNothingSelected(parent: AdapterView<*>?) {
                            selectedCategoryId = 0
                            selectedSubCategoryId = 0
                        }
                    }
                }
            }
        }

        binding.btnChooseFile.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE)
        }

        binding.btnAddEntry.setOnClickListener {
            val name = binding.transName.text.toString()
            val amount = binding.transAmount.text.toString().toDoubleOrNull() ?: 0.0
            val method = binding.transMethod.text.toString()
            val location = binding.transLocation.text.toString()
            val date = binding.transDate.text.toString()
            val description = binding.transDescription.text.toString()
            val transactionType = if (binding.rbExpense.isChecked) "Expense" else "Income"

            if (userId != 0 && selectedCategoryId != 0) {
                val transaction = Transaction(
                    userId = userId,
                    name = name,
                    amount = amount,
                    transactionMethod = method,
                    location = location,
                    dateTime = date,
                    description = description,
                    photo = "@drawable/receipt.jpeg",
                    categoryId = selectedCategoryId,
                    subCategoryId = if (selectedSubCategoryId != 0) selectedSubCategoryId else null,
                    expense = binding.rbExpense.isChecked,
                    recurring = false
                )

                lifecycleScope.launch {
                    database.transactionDao().insert(transaction)
                    Toast.makeText(requireContext(), "Transaction saved successfully", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(requireContext(), "Please select a category first!", Toast.LENGTH_SHORT).show()
            }

            // Reset the fields after saving
            resetFields()
        }

        val calendar = Calendar.getInstance()

        binding.btnPickDate.setOnClickListener {
            val datePicker = DatePickerDialog(requireContext(), { _, year, month, dayOfMonth ->
                selectedDate = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth)
                updateDateTimeField()
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
            datePicker.show()
        }

        binding.btnPickTime.setOnClickListener {
            val timePicker = TimePickerDialog(requireContext(), { _, hourOfDay, minute ->
                selectedTime = String.format("%02d:%02d", hourOfDay, minute)
                updateDateTimeField()
            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true)
            timePicker.show()
        }
    }

    private fun updateDateTimeField() {
        if (selectedDate.isNotEmpty() && selectedTime.isNotEmpty()) {
            val dateTime = "$selectedDate $selectedTime"
            binding.transDate.setText(dateTime)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_PICK_IMAGE && resultCode == AppCompatActivity.RESULT_OK && data != null) {
            val selectedImageUri = data.data

            selectedImagePath = getFilePathFromUri(selectedImageUri)

            Glide.with(requireContext())
                .load(selectedImageUri)
                .into(binding.imageView)

            val fileName = getFileNameFromUri(selectedImageUri)
            binding.tvFileName.text = fileName
        }
    }

    private fun getFileNameFromUri(uri: Uri?): String {
        var fileName = ""
        uri?.let {
            val cursor = requireContext().contentResolver.query(uri, null, null, null, null)
            cursor?.apply {
                moveToFirst()
                val columnIndex = getColumnIndex(OpenableColumns.DISPLAY_NAME)
                fileName = getString(columnIndex)
                close()
            }
        }
        return fileName
    }

    private fun getFilePathFromUri(uri: Uri?): String? {
        var filePath: String? = null
        uri?.let {
            val projection = arrayOf(MediaStore.Images.Media.DATA)
            val cursor: Cursor? = requireContext().contentResolver.query(uri, projection, null, null, null)
            cursor?.use {
                if (it.moveToFirst()) {
                    val columnIndex = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                    filePath = it.getString(columnIndex)
                }
            }
        }
        return filePath
    }

    private fun resetFields() {
        // Reset all the fields to empty
        binding.transName.text.clear()
        binding.transAmount.text.clear()
        binding.transMethod.text.clear()
        binding.transLocation.text.clear()
        binding.transDate.text.clear()
        binding.transDescription.text.clear()
        binding.rbExpense.isChecked = true  // Reset the radio button to Expense
        binding.imageView.setImageDrawable(null)  // Clear the selected image
        binding.tvFileName.text = ""  // Clear the file name text
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
