package com.example.smartmealplanner.ui.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.smartmealplanner.adapter.FavoriteRecipeAdapter
import com.example.smartmealplanner.data.api.ApiService
import com.example.smartmealplanner.data.api.RetrofitClient
import com.example.smartmealplanner.data.api.TokenManager
import com.example.smartmealplanner.data.model.Recipe
import com.example.smartmealplanner.databinding.ActivityFavoriteRecipesBinding
import com.example.smartmealplanner.databinding.LayoutExpandableSectionBinding
import com.example.smartmealplanner.ui.viewmodel.FavoriteViewModel
import com.example.smartmealplanner.ui.viewmodel.FavoriteViewModelFactory


class FavoriteRecipesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteRecipesBinding
    
    private val viewModel: FavoriteViewModel by viewModels {
        val tokenManager = TokenManager(this)
        val apiService = RetrofitClient.create(ApiService::class.java, tokenManager)
        FavoriteViewModelFactory(apiService)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteRecipesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        observeViewModel()
        
        viewModel.fetchFavorites()
    }

    private fun setupToolbar() {
        binding.btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun observeViewModel() {
        viewModel.favoriteRecipes.observe(this) { recipes ->
            setupSections(recipes)
        }

        viewModel.errorMessage.observe(this) { message ->
            message?.let {
                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
            }
        }
        
        viewModel.isLoading.observe(this) { isLoading ->
            // Update loading state if UI has a progress bar
        }
    }

    private fun setupSections(allFavorites: List<Recipe>) {
        // Clear the container first to avoid duplicates
        binding.llSectionsContainer.removeAllViews()

        if (allFavorites.isEmpty()) {
            // Optional: Show empty state if needed
            return
        }

        // Group recipes by cuisine
        val groupedRecipes = allFavorites.groupBy { it.cuisine }

        groupedRecipes.forEach { (cuisine, recipes) ->
            // Dynamically inflate the section layout
            val sectionBinding = LayoutExpandableSectionBinding.inflate(
                LayoutInflater.from(this),
                binding.llSectionsContainer,
                false
            )

            // Setup the inflated section
            setupExpandableSection(sectionBinding, cuisine, recipes)

            // Add the section to the main container
            binding.llSectionsContainer.addView(sectionBinding.root)
        }
    }

    private fun setupExpandableSection(
        sectionBinding: LayoutExpandableSectionBinding,
        title: String,
        recipes: List<Recipe>
    ) {
        sectionBinding.tvCategory.text = title
        sectionBinding.tvCount.text = recipes.size.toString()

        val recipeAdapter = FavoriteRecipeAdapter(recipes)

        sectionBinding.rvRecipes.apply {
            layoutManager = GridLayoutManager(this@FavoriteRecipesActivity, 2)
            adapter = recipeAdapter
            isNestedScrollingEnabled = false
            this.visibility = View.GONE
        }

        sectionBinding.btnSeeAll.visibility = View.GONE
        sectionBinding.btnSeeAll.text = "See All"

        sectionBinding.btnSeeAll.setOnClickListener {
            recipeAdapter.isExpanded = !recipeAdapter.isExpanded
            sectionBinding.btnSeeAll.text = if (recipeAdapter.isExpanded) "Show Less" else "See All"
        }

        val toggleClick = View.OnClickListener {
            toggleEntireSection(sectionBinding, recipeAdapter, recipes.size > 4)
        }

        sectionBinding.tvCategory.setOnClickListener(toggleClick)
        sectionBinding.ivArrow.setOnClickListener(toggleClick)
        sectionBinding.ivArrow.rotation = 0f
    }

    private fun toggleEntireSection(
        sectionBinding: LayoutExpandableSectionBinding,
        adapter: FavoriteRecipeAdapter,
        hasMore: Boolean
    ) {
        val isCurrentlyVisible = sectionBinding.rvRecipes.visibility == View.VISIBLE

        if (isCurrentlyVisible) {
            sectionBinding.rvRecipes.visibility = View.GONE
            sectionBinding.btnSeeAll.visibility = View.GONE
            sectionBinding.ivArrow.animate().rotation(0f).setDuration(200).start()
            adapter.isExpanded = false
            sectionBinding.btnSeeAll.text = "See All"
        } else {
            sectionBinding.rvRecipes.visibility = View.VISIBLE
            if (hasMore) {
                sectionBinding.btnSeeAll.visibility = View.VISIBLE
            }
            sectionBinding.ivArrow.animate().rotation(180f).setDuration(200).start()
        }
    }
}
