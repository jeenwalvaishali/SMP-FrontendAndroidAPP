package com.example.smartmealplanner.data.model

import com.google.gson.annotations.SerializedName

data class Category(
    @SerializedName("_id") val id: String? = null,
    val name: String,
    val imageUrl: String? = null,
    var isSelected: Boolean = false
)