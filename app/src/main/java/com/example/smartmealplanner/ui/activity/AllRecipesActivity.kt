package com.example.smartmealplanner.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smartmealplanner.R
import com.example.smartmealplanner.adapter.AllRecipesAdapter
import com.example.smartmealplanner.data.api.ApiService
import com.example.smartmealplanner.data.api.RetrofitClient
import com.example.smartmealplanner.data.api.TokenManager
import com.example.smartmealplanner.data.model.Recipe
import com.example.smartmealplanner.ui.common.OnItemClickListener
import com.example.smartmealplanner.ui.viewmodel.AllRecipesViewModel
import com.example.smartmealplanner.ui.viewmodel.AllRecipesViewModelFactory

class AllRecipesActivity : AppCompatActivity(), OnItemClickListener {

    private lateinit var allRecipesRecycler: RecyclerView
    private lateinit var backButton: ImageView
    private lateinit var titleText: TextView
    private lateinit var adapter: AllRecipesAdapter

    private val viewModel: AllRecipesViewModel by viewModels {
        val tokenManager = TokenManager(this)
        val apiService = RetrofitClient.create(ApiService::class.java, tokenManager)
        AllRecipesViewModelFactory(apiService)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_recipes)

        initViews()
        setupRecyclerView()
        observeViewModel()

        val searchQuery = intent.getStringExtra("SEARCH_QUERY")
        if (searchQuery != null) {
            titleText.text = "Results for \"$searchQuery\""
            viewModel.searchRecipes(searchQuery)
        } else {
            val recipes = intent.getSerializableExtra("RECIPES_LIST") as? ArrayList<Recipe> ?: arrayListOf()
            viewModel.setRecipes(recipes)
        }
    }

    private fun initViews() {
        allRecipesRecycler = findViewById(R.id.allRecipesRecycler)
        backButton = findViewById(R.id.backButton)
        titleText = findViewById(R.id.titleText)

        backButton.setOnClickListener {
            finish()
        }
    }

    private fun setupRecyclerView() {
        adapter = AllRecipesAdapter(emptyList(), this)
        allRecipesRecycler.layoutManager = GridLayoutManager(this, 2)
        allRecipesRecycler.adapter = adapter
    }

    private fun observeViewModel() {
        viewModel.recipes.observe(this) { recipes ->
            adapter.updateData(recipes)
        }
    }

    override fun onItemClick(position: Int) {
        // Handle if needed
    }

    override fun onRecipeClick(recipe: Recipe) {
        val intent = Intent(this, RecipeActivity::class.java)
        intent.putExtra("RECIPE_DATA", recipe)
        startActivity(intent)
    }
}