package com.example.smartmealplanner.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartmealplanner.data.model.MealPlanRequest
import com.example.smartmealplanner.data.model.MealPlanResponse
import com.example.smartmealplanner.data.repository.MealPlanRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class MealPlanState {
    object Loading : MealPlanState()
    data class Success(val mealPlan: MealPlanResponse) : MealPlanState()
    data class Error(val message: String) : MealPlanState()
}

class MealPlanViewModel(private val repository: MealPlanRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<MealPlanState?>(null)
    val uiState: StateFlow<MealPlanState?> = _uiState.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<MealPlanResponse>()
    val navigationEvent: SharedFlow<MealPlanResponse> = _navigationEvent.asSharedFlow()

    fun generateMealPlan(request: MealPlanRequest) {
        viewModelScope.launch {
            _uiState.value = MealPlanState.Loading
            try {
                Log.d("MealPlanVM", "Sending request: $request")
                val response = repository.generateMealPlan(request)
                if (response.isSuccessful && response.body() != null) {
                    val mealPlan = response.body()!!
                    _uiState.value = MealPlanState.Success(mealPlan)
                    _navigationEvent.emit(mealPlan)
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("MealPlanVM", "Error 422/Other: $errorBody")
                    _uiState.value = MealPlanState.Error(errorBody ?: "Failed to generate meal plan")
                }
            } catch (e: Exception) {
                Log.e("MealPlanVM", "Exception: ${e.message}", e)
                _uiState.value = MealPlanState.Error(e.message ?: "An unexpected error occurred")
            }
        }
    }
}
