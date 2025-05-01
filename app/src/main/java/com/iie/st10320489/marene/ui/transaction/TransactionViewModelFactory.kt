package com.iie.st10320489.marene.ui.transaction

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.iie.st10320489.marene.data.database.DatabaseInstance
import com.iie.st10320489.marene.data.repository.TransactionRepository

class TransactionViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TransactionViewModel::class.java)) {
            val transactionDao = DatabaseInstance.getDatabase(context).transactionDao()
            val repository = TransactionRepository(transactionDao) // FIXED here
            return TransactionViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
