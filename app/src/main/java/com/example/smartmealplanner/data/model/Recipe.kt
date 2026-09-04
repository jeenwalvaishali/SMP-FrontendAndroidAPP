package com.example.smartmealplanner.data.model

import java.io.Serializable
import com.google.gson.annotations.SerializedName

data class Recipe(
    @SerializedName("_id") val id: String? = null,
    val title: String,
    val description: String,
    val ingredients: List<String>,
    @SerializedName("steps") val directions: List<String>,
    val cuisine: String,
    val prepTime: Int,
    val imageUrl: String,
    val avgRating: Double = 0.0,
    val tags: List<String> = emptyList()
) : Serializable {
    // Helper property to maintain compatibility with existing UI logic
    val cookingTime: String
        get() = "$prepTime mins"
}

//data class Recipe(
//    @SerializedName("_id") val id: String? = null,
//    val title: String,
//    val description: String,
//    val ingredients: List<String>,
//    @SerializedName("steps") val directions: List<String>,
//    val cuisine: String,
//    val prepTime: Int,
//    val imageUrl: String,
//    val avgRating: Double = 0.0,
//    val tags: List<String> = emptyList()
//) {
//    // Helper property to maintain compatibility with existing UI logic
//    val cookingTime: String
//        get() = "$prepTime mins"
//}