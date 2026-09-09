package com.example.smartmealplanner.data.repository

import com.example.smartmealplanner.data.api.MealPlanApiService
import com.example.smartmealplanner.data.model.MealPlanRequest

class MealPlanRepository(
    private val api: MealPlanApiService
) {

    suspend fun generateMealPlan(request: MealPlanRequest) =
        api.generateMealPlan(request)

}
