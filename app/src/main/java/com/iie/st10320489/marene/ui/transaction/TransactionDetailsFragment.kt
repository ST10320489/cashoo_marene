package com.iie.st10320489.marene.ui.transaction

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.iie.st10320489.marene.R
import com.iie.st10320489.marene.databinding.FragmentTransactionDetailsBinding
import com.iie.st10320489.marene.data.database.DatabaseInstance
import kotlinx.coroutines.launch

class TransactionDetailsFragment : Fragment() {

    private var _binding: FragmentTransactionDetailsBinding? = null
    private val binding get() = _binding!!

    private var transactionId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        transactionId = arguments?.getInt("transactionId") ?: 0
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentTransactionDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadTransactionDetails()
    }

    private fun loadTransactionDetails() {
        val db = DatabaseInstance.getDatabase(requireContext())
        lifecycleScope.launch {
            val transactionWithCategory = db.transactionDao().getTransactionWithCategoryById(transactionId)

            transactionWithCategory?.let { item ->
                val t = item.transaction
                binding.txtName.text = t.name
                binding.txtAmount.text = if (t.expense) "-R${t.amount}" else "+R${t.amount}"
                binding.txtDateTime.text = t.dateTime
                binding.txtMethod.text = t.transactionMethod
                binding.txtLocation.text = "${t.location ?: "N/A"}"
                binding.txtDescription.text = "${t.description ?: "N/A"}"
                binding.txtCategory.text = "${item.category.name}"
                binding.txtSubCategory.text = "${item.category?.name ?: "N/A"}"
                binding.imgTransactionPhoto.setImageResource(R.drawable.receipt)


            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
