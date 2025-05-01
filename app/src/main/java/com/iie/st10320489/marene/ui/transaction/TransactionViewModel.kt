package com.iie.st10320489.marene.ui.transaction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iie.st10320489.marene.data.entities.TransactionWithCategory
import com.iie.st10320489.marene.data.repository.TransactionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TransactionViewModel(private val repository: TransactionRepository) : ViewModel() {

    private val _transactions = MutableStateFlow<List<TransactionWithCategory>>(emptyList())
    val transactions: StateFlow<List<TransactionWithCategory>> = _transactions

    fun loadTransactions(userId: Int) {
        viewModelScope.launch {
            _transactions.value = repository.getTransactionsByUserId(userId)
        }
    }
}

