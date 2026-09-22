package com.example.smartmealplanner.data.api

import com.example.smartmealplanner.data.model.*
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @GET("recipes")
    suspend fun getAllRecipes(@Query("category") category: String? = null): RecipeResponse

    @GET("recipes/search")
    suspend fun searchRecipes(@Query("q") query: String): SearchResponse

    @GET("recipes/{id}")
    suspend fun getRecipeById(@Path("id") id: String): SingleRecipeResponse

    @GET("recipes/categories")
    suspend fun getAllCategories(): CategoryResponse

    @GET("recipes/recommended")
    suspend fun getRecommendedRecipes(@Query("category") category: String? = null): RecipeResponse

    @GET("recipes/week")
    suspend fun getRecipeOfTheWeek(): SingleRecipeResponse
    
    @GET("recipes/favorites")
    suspend fun getFavoriteRecipes(): RecipeResponse

    @POST("recipes/favorites/{id}")
    suspend fun addToFavorites(@Path("id") id: String): Response<ResponseBody>

    @DELETE("recipes/favorites/{id}")
    suspend fun removeFromFavorites(@Path("id") id: String): Response<ResponseBody>

    @POST("meal-plan/generate")
    suspend fun generateMealPlan(@Body preferences: MealPreferences): WeeklyMealPlan

    @POST("ai/chat")
    suspend fun sendMessage(@Body request: ChatRequest): ChatResponse

    @POST("meal-plan/replace")
    suspend fun replaceMeal(@Body request: UpdateMealPlanRequest): UpdateMealPlanResponse
}
