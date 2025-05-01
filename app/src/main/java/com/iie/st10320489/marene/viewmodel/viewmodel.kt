package com.iie.st10320489.marene.viewmodel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import com.iie.st10320489.marene.data.repository.*
import com.iie.st10320489.marene.data.entities.*

class UserViewModel(private val repo: UserRepository) : ViewModel() {
    val allUsers = repo.getAllUsers().stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    fun insert(user: User) = viewModelScope.launch { repo.insertUser(user) }
}

class UserSettingsViewModel(private val repo: UserSettingsRepository) : ViewModel() {
    val all = repo.getAllSettings().stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    fun insert(settings: UserSettings) = viewModelScope.launch {
        repo.insertUserSettings(settings)
    }
}

class TransactionViewModel(private val repo: TransactionRepository) : ViewModel() {
    val all = repo.getAll().stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    fun insert(transaction: Transaction) = viewModelScope.launch { repo.insert(transaction) }
}

class CategoryViewModel(private val repo: CategoryRepository) : ViewModel() {
    val all = repo.getAll()
    fun insert(category: Category) = viewModelScope.launch { repo.insert(category) }
}

class SubCategoryViewModel(private val repo: SubCategoryRepository) : ViewModel() {
    val all = repo.getAll() // LiveData, no stateIn
    fun insert(subCategory: SubCategory) = viewModelScope.launch { repo.insert(subCategory) }
}


class RewardViewModel(private val repo: RewardRepository) : ViewModel() {
    val all = repo.getAll().stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    fun insert(reward: Reward) = viewModelScope.launch { repo.insert(reward) }
}

class RewardHistoryViewModel(private val repo: RewardHistoryRepository) : ViewModel() {
    val all = repo.getAll().stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    fun insert(history: RewardHistory) = viewModelScope.launch { repo.insert(history) }
}

class ActivityLogViewModel(private val repo: ActivityLogRepository) : ViewModel() {
    val all = repo.getAll().stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    fun insert(log: ActivityLog) = viewModelScope.launch { repo.insert(log) }
}