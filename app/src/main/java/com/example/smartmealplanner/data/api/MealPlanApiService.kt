package com.example.smartmealplanner.data.api

import com.example.smartmealplanner.data.model.MealPlanRequest
import com.example.smartmealplanner.data.model.MealPlanResponse
import com.example.smartmealplanner.data.model.SavedMealPlan
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface MealPlanApiService {

    @POST("meal-plans/generate")
    suspend fun generateMealPlan(
        @Body request: MealPlanRequest
    ): Response<MealPlanResponse>

    @GET("meal-plans")
    suspend fun getSavedMealPlan(): Response<SavedMealPlan>

}
