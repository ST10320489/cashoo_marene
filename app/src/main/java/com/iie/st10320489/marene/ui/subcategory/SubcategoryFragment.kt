package com.iie.st10320489.marene.ui.subcategory

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.iie.st10320489.marene.R
import com.iie.st10320489.marene.data.database.AppDatabase
import com.iie.st10320489.marene.data.database.DatabaseInstance
import com.iie.st10320489.marene.data.entities.SubCategory
import com.iie.st10320489.marene.databinding.FragmentSubcategoryBinding
import kotlinx.coroutines.launch

class SubcategoryFragment : Fragment() {

    private var _binding: FragmentSubcategoryBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: SubcategoryAdapter
    private lateinit var database: AppDatabase
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSubcategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        database = DatabaseInstance.getDatabase(requireContext())
        sharedPreferences = requireActivity().getSharedPreferences("UserPreferences", Context.MODE_PRIVATE)

        adapter = SubcategoryAdapter(mutableListOf()) { selectedSubcategory ->
            val bundle = Bundle().apply {
                putString("categoryName", selectedSubcategory.name)
                putString("subCategoryName", selectedSubcategory.name)
            }
            findNavController().navigate(R.id.action_subcategoryFragment_to_filterFragment, bundle)
        }

        binding.recyclerViewSubcategories.layoutManager = LinearLayoutManager(context)
        binding.recyclerViewSubcategories.adapter = adapter

        loadSubcategories()
    }

    fun loadSubcategories() {
        lifecycleScope.launch {
            sharedPreferences = requireActivity().getSharedPreferences("UserPreferences", Context.MODE_PRIVATE)
            val currentUserEmail = sharedPreferences.getString("currentUserEmail", null)

            if (currentUserEmail != null) {
                val userId = database.userDao().getUserIdByEmail(currentUserEmail)

                if (userId != null) {
                    val subcategoriesFromDb = database.subCategoryDao().getSubCategoriesForUser(userId)
                    adapter.updateList(subcategoriesFromDb)
                }
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
