package com.example.smartmealplanner.data.repository

import com.example.smartmealplanner.data.api.ApiService
import com.example.smartmealplanner.data.model.MealPreferences
import com.example.smartmealplanner.data.model.WeeklyMealPlan

class MealRepository(private val apiService: ApiService) {

    suspend fun generateMealPlan(preferences: MealPreferences): Result<WeeklyMealPlan> {
        return try {
            val response = apiService.generateMealPlan(preferences)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
