package com.example.smartmealplanner.data.model

import java.io.Serializable

// Matches the 'preferences' object in your Mongoose Schema
data class MealPlanRequest(
    val dietType: String,
    val dailyCalories: Int,
    val mealsPerDay: Int,
    val cuisine: String,
    val maxCookingTime: Int
)

// Matches the expected generated response structure
data class MealPlanResponse(
    val dailyCaloriesTarget: Int,
    val days: List<DayPlan>
) : Serializable

data class DayPlan(
    val day: String,
    val meals: List<Meal>,
    val totalCalories: Int
) : Serializable

data class Meal(
    val mealType: String, // BREAKFAST, LUNCH, DINNER, SNACK
    val recipe: Recipe
) : Serializable
