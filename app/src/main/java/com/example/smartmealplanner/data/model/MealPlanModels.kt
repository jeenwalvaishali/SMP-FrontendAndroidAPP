package com.example.smartmealplanner.data.model

import java.io.Serializable
import com.google.gson.annotations.SerializedName

// Matches the 'preferences' object in your Mongoose Schema
data class MealPlanRequest(
    val dietType: String,
    val dailyCalories: Int,
    val mealsPerDay: Int,
    val cuisine: String,
    val maxCookingTime: Int
) : Serializable

// Matches the expected generated response structure (from /generate)
data class MealPlanResponse(
    val dailyCaloriesTarget: Int,
    val days: List<DayPlan>
) : Serializable

// Matches the saved meal plan structure (from GET /meal-plans)
data class SavedMealPlan(
    @SerializedName("_id") val id: String,
    val user: String,
    val weekStartDate: String,
    val preferences: MealPlanRequest,
    val days: List<DayPlan>,
    val createdAt: String,
    val updatedAt: String
) : Serializable

data class DayPlan(
    @SerializedName("_id") val id: String? = null,
    val day: String,
    val meals: List<Meal>,
    val totalCalories: Int
) : Serializable

data class Meal(
    @SerializedName("_id") val id: String? = null,
    val mealType: String, // BREAKFAST, LUNCH, DINNER, SNACK
    val recipe: Recipe
) : Serializable
