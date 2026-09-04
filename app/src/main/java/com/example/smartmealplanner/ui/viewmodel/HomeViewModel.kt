package com.example.smartmealplanner.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartmealplanner.data.api.ApiService
import com.example.smartmealplanner.data.model.Category
import com.example.smartmealplanner.data.model.Recipe
import kotlinx.coroutines.launch

class HomeViewModel(private val apiService: ApiService) : ViewModel() {

    private val _categories = MutableLiveData<List<Category>>()
    val categories: LiveData<List<Category>> = _categories

    private val _recommendedRecipes = MutableLiveData<List<Recipe>>()
    val recommendedRecipes: LiveData<List<Recipe>> = _recommendedRecipes

    private val _recipesOfWeek = MutableLiveData<List<Recipe>>()
    val recipesOfWeek: LiveData<List<Recipe>> = _recipesOfWeek

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    fun fetchData() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                // Fetch data from API
                val categoryResponse = apiService.getAllCategories()
                val recommendedResponse = apiService.getRecommendedRecipes()
                val weekResponse = apiService.getRecipeOfTheWeek()

                // Map the list of strings to Category objects
                val categoryList = categoryResponse.data.map { name ->
                    Category(name = name)
                }

                _categories.value = categoryList
                _recommendedRecipes.value = recommendedResponse.data
                _recipesOfWeek.value = listOf(weekResponse.data)
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "An error occurred"
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun filterByCategory(categoryName: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // If "All" is selected (assuming the first category might be All or handle empty/null)
                // Adjust based on your API's expected behavior for all categories.
                val categoryParam = if (categoryName.equals("All", ignoreCase = true)) null else categoryName
                val response = apiService.getRecommendedRecipes(categoryParam)
                _recommendedRecipes.value = response.data
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Failed to filter recipes"
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }
}