package com.example.smartmealplanner.ui.activity

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.smartmealplanner.R
import com.example.smartmealplanner.data.api.ApiService
import com.example.smartmealplanner.data.api.RetrofitClient
import com.example.smartmealplanner.data.api.TokenManager
import com.example.smartmealplanner.data.model.Recipe
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.launch

class RecipeActivity : AppCompatActivity() {
    private lateinit var tokenManager: TokenManager
    private lateinit var apiService: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_detail)

        tokenManager = TokenManager(this)
        apiService = RetrofitClient.create(ApiService::class.java, tokenManager)

        // Get the recipe data from the Intent
        val recipe = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra("RECIPE_DATA", Recipe::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getSerializableExtra("RECIPE_DATA") as Recipe
        }

        if (recipe != null) {
            displayRecipeData(recipe)
            setupFavoriteButton(recipe)
        }

        setupCollapsibleSections()
    }

    private fun setupFavoriteButton(recipe: Recipe) {
        val btnAddToFav = findViewById<MaterialButton>(R.id.btnAddToFav)
        btnAddToFav.setOnClickListener {
            val recipeId = recipe.id
            if (recipeId != null) {
                addToFavorites(recipeId)
            } else {
                Toast.makeText(this, "Recipe ID not found", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun addToFavorites(recipeId: String) {
        lifecycleScope.launch {
            try {
                val response = apiService.addToFavorites(recipeId)
                if (response.isSuccessful) {
                    Toast.makeText(this@RecipeActivity, "Added to favorites!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@RecipeActivity, "Failed to add to favorites: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@RecipeActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun displayRecipeData(recipe: Recipe) {
        val tvTitle = findViewById<TextView>(R.id.tvTitle)
        val tvRating = findViewById<TextView>(R.id.tvRating)
        val tvDescription = findViewById<TextView>(R.id.tvDescription)
        val ivRecipeDetail = findViewById<ImageView>(R.id.ivRecipeDetail)
        val tvPrepTime = findViewById<TextView>(R.id.tvPrepTime)
        val tvCuisine = findViewById<TextView>(R.id.tvCuisine)

        // Updating UI with recipe data
        tvTitle.text = recipe.title
        tvRating.text = recipe.avgRating.toString()
        tvDescription.text = recipe.description
        tvPrepTime.text = recipe.cookingTime
        tvCuisine.text = recipe.cuisine
        
        // Load image using Glide
        Glide.with(this)
            .load(recipe.imageUrl)
            .placeholder(R.drawable.macarons)
            .error(R.drawable.macarons)
            .into(ivRecipeDetail)

        // Ingredients
        val tvIngredientsContent = findViewById<TextView>(R.id.tvIngredientsContent)
        tvIngredientsContent.text = recipe.ingredients.joinToString("\n") { "• $it" }

        // Directions
        val tvDirectionsContent = findViewById<TextView>(R.id.tvDirectionsContent)
        tvDirectionsContent.text = recipe.directions.mapIndexed { index, step ->
            "${index + 1}. $step"
        }.joinToString("\n\n")
    }

    private fun setupCollapsibleSections() {
        // 1. Ingredients Section
        val tvIngredientsHeader = findViewById<TextView>(R.id.tvIngredients)
        val layoutIngredients = findViewById<LinearLayout>(R.id.layoutIngredientsContent)

        // 2. Directions Section
        val tvDirectionsHeader = findViewById<TextView>(R.id.tvDirections)
        val layoutDirections = findViewById<LinearLayout>(R.id.layoutDirectionsContent)
        val tvDirectionsContent = findViewById<TextView>(R.id.tvDirectionsContent)
        val tvReadMore = findViewById<TextView>(R.id.tvReadMore)

        tvIngredientsHeader.setOnClickListener {
            if (layoutIngredients.visibility == View.GONE) {
                layoutIngredients.visibility = View.VISIBLE
                tvIngredientsHeader.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_chevron_up, 0)
            } else {
                layoutIngredients.visibility = View.GONE
                tvIngredientsHeader.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_chevron_down, 0)
            }
        }

        tvDirectionsHeader.setOnClickListener {
            if (layoutDirections.visibility == View.GONE) {
                layoutDirections.visibility = View.VISIBLE
                tvDirectionsHeader.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_chevron_up, 0)
            } else {
                layoutDirections.visibility = View.GONE
                tvDirectionsHeader.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_chevron_down, 0)
            }
        }

        // 3. Read More (Inside Directions)
        tvReadMore.setOnClickListener {
            if (tvDirectionsContent.maxLines == 6) {
                tvDirectionsContent.maxLines = Int.MAX_VALUE
                tvReadMore.text = getString(R.string.show_less)
            } else {
                tvDirectionsContent.maxLines = 6
                tvReadMore.text = getString(R.string.read_more)
            }
        }
    }
}
