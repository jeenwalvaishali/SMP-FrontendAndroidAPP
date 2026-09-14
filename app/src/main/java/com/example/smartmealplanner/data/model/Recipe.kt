package com.example.smartmealplanner.data.model

import java.io.Serializable
import com.google.gson.annotations.SerializedName

data class Recipe(
    @SerializedName("_id") val id: String? = null,
    val title: String,
    val description: String,
    val ingredients: List<String>,
    val steps: List<String>,
    val cuisine: String,
    val dietType: String? = null,
    val mealTypes: List<String>? = null,
    val calories: Int? = 0,
    val protein: Int? = 0,
    val carbohydrates: Int? = 0,
    val fat: Int? = 0,
    val prepTime: Int,
    val imageUrl: String,
    val avgRating: Double = 0.0,
    val tags: List<String> = emptyList()
) : Serializable {
    // Helper property to maintain compatibility with existing UI logic
    val cookingTime: String
        get() = "$prepTime mins"
}
