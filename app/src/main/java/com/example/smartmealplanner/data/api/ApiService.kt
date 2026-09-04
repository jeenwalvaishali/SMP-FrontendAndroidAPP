package com.example.smartmealplanner.data.api

import com.example.smartmealplanner.data.model.CategoryResponse
import com.example.smartmealplanner.data.model.RecipeResponse
import com.example.smartmealplanner.data.model.SearchResponse
import com.example.smartmealplanner.data.model.SingleRecipeResponse
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

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
}