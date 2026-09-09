package com.example.smartmealplanner.data.model

import java.io.Serializable

data class WeeklyMealPlan(
    val monday: DayMealPlan,
    val tuesday: DayMealPlan,
    val wednesday: DayMealPlan,
    val thursday: DayMealPlan,
    val friday: DayMealPlan,
    val saturday: DayMealPlan,
    val sunday: DayMealPlan,
    val totalWeeklyCalories: Int
) : Serializable

data class DayMealPlan(
    val breakfast: MealInfo,
    val lunch: MealInfo,
    val dinner: MealInfo,
    val totalDayCalories: Int
) : Serializable

data class MealInfo(
    val recipeId: String,
    val recipeTitle: String,
    val calories: Int,
    val imageUrl: String
) : Serializable
