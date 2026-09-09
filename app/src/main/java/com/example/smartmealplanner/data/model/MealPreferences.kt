package com.example.smartmealplanner.data.model

import java.io.Serializable

data class MealPreferences(
    val diet: String,
    val dailyCalories: Int,
    val mealsPerDay: Int,
    val cuisine: String,
    val cookingTime: String
) : Serializable
