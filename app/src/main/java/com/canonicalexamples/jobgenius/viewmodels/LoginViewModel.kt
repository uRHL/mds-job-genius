package com.canonicalexamples.jobgenius.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.canonicalexamples.jobgenius.model.JobDatabase
import com.canonicalexamples.jobgenius.model.user.User
import com.canonicalexamples.jobgenius.model.user.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginViewModel(private val database: JobDatabase) : ViewModel() {

    private val repository: UserRepository = UserRepository(database.userDao())
    val userList: LiveData<List<User>> = repository.fetchUsers

    fun addUser(user: User){
        viewModelScope.launch(Dispatchers.IO) {
            repository.addUser(user)
        }
    }
}

class LoginViewModelFactory(private val database: JobDatabase) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LoginViewModel(database) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}