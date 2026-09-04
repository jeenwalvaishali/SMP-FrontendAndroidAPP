package com.example.smartmealplanner.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.smartmealplanner.data.api.TokenManager
import com.example.smartmealplanner.data.repository.AuthRepository

class LoginViewModelFactory(
    private val repository: AuthRepository,
    private val tokenManager: TokenManager
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            LoginViewModel(repository, tokenManager) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}