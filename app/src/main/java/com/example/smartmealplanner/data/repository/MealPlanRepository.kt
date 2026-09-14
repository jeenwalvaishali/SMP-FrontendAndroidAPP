package com.example.smartmealplanner.data.repository

import com.example.smartmealplanner.data.api.MealPlanApiService
import com.example.smartmealplanner.data.model.MealPlanRequest
import com.example.smartmealplanner.data.model.SavedMealPlan
import retrofit2.Response

class MealPlanRepository(
    private val api: MealPlanApiService
) {

    suspend fun generateMealPlan(request: MealPlanRequest) =
        api.generateMealPlan(request)

    suspend fun getSavedMealPlan(): Response<SavedMealPlan> =
        api.getSavedMealPlan()

}
