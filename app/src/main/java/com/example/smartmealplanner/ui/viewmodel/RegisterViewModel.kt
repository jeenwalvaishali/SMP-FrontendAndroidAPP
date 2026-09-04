package com.example.smartmealplanner.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartmealplanner.data.api.TokenManager
import com.example.smartmealplanner.data.model.AuthResponse
import com.example.smartmealplanner.data.repository.AuthRepository
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val repo: AuthRepository,
    private val tokenManager: TokenManager
) : ViewModel() {

    val registerState = MutableLiveData<Result<AuthResponse>>()

    fun register(name: String, email: String, password: String) {
        viewModelScope.launch {
            val result = repo.register(name, email, password)

            result.onSuccess {
                tokenManager.saveToken(it.token)
            }

            registerState.postValue(result)
        }
    }
}