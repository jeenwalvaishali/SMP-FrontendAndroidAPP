package com.example.smartmealplanner.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartmealplanner.data.api.ApiService
import com.example.smartmealplanner.data.model.Recipe

import kotlinx.coroutines.launch

class FavoriteViewModel(private val apiService: ApiService) : ViewModel() {

    private val _favoriteRecipes = MutableLiveData<List<Recipe>>()
    val favoriteRecipes: LiveData<List<Recipe>> = _favoriteRecipes

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    fun fetchFavorites() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                val response = apiService.getFavoriteRecipes()
                _favoriteRecipes.value = response.data
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Failed to fetch favorites"
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }
}