package com.example.smartmealplanner.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartmealplanner.data.api.ApiService
import com.example.smartmealplanner.data.model.Recipe
import kotlinx.coroutines.launch

class AllRecipesViewModel(private val apiService: ApiService) : ViewModel() {

    private val _recipes = MutableLiveData<List<Recipe>>()
    val recipes: LiveData<List<Recipe>> = _recipes

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    fun searchRecipes(query: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                val response = apiService.searchRecipes(query)
                _recipes.value = response.data
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Failed to load search results"
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun setRecipes(recipeList: List<Recipe>) {
        _recipes.value = recipeList
    }
}