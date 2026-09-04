package com.example.smartmealplanner.data.model

data class SearchResponse(
    val success: Boolean,
    val total: Int,
    val page: Int,
    val pages: Int,
    val data: List<Recipe>
)