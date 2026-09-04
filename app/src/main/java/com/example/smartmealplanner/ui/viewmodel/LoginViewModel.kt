package com.example.smartmealplanner.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartmealplanner.data.api.TokenManager
import com.example.smartmealplanner.data.model.AuthResponse
import com.example.smartmealplanner.data.repository.AuthRepository
import kotlinx.coroutines.launch

class LoginViewModel(
    private val repo: AuthRepository,
    private val tokenManager: TokenManager
) : ViewModel() {

    val loginState = MutableLiveData<Result<AuthResponse>>()

    fun login(email: String, password: String) {
        viewModelScope.launch {
            val result = repo.login(email, password)

            result.onSuccess {
                tokenManager.saveToken(it.token)
            }

            loginState.postValue(result)
        }
    }
}