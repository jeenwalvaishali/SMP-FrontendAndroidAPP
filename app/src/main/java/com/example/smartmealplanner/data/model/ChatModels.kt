package com.example.smartmealplanner.data.model

data class ChatRequest(
    val message: String
)

data class ChatResponse(
    val success: Boolean,
    val answer: String
)

data class UpdateMealPlanRequest(
    val mealPlanId: String,
    val day: String,
    val mealType: String,
    val newRecipeId: String
)

data class UpdateMealPlanResponse(
    val mealType: String,
    val recipe: PartialRecipe
)

data class PartialRecipe(
    val id: String,
    val title: String,
    val calories: Int,
    val prepTime: Int
)

data class ChatMessage(
    val text: String,
    val isUser: Boolean,
    val timestamp: Long = System.currentTimeMillis()
)
