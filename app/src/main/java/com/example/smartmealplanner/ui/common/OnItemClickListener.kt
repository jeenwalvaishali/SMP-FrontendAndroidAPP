package com.example.smartmealplanner.ui.common

import com.example.smartmealplanner.data.model.Recipe

interface OnItemClickListener {
    fun onItemClick(position: Int)
    fun onRecipeClick(recipe: Recipe)
}