package com.example.smartmealplanner.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smartmealplanner.R
import com.example.smartmealplanner.adapter.CategoryAdapter
import com.example.smartmealplanner.adapter.RecipeWeekAdapter
import com.example.smartmealplanner.adapter.RecommendationAdapter
import com.example.smartmealplanner.data.api.ApiService
import com.example.smartmealplanner.data.api.RetrofitClient
import com.example.smartmealplanner.data.api.TokenManager
import com.example.smartmealplanner.data.model.Recipe
import com.example.smartmealplanner.ui.common.OnItemClickListener
import com.example.smartmealplanner.ui.viewmodel.HomeViewModel
import com.example.smartmealplanner.ui.viewmodel.HomeViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.card.MaterialCardView
import kotlinx.coroutines.launch

class HomeActivity : AppCompatActivity(), OnItemClickListener {

    private lateinit var recommendationRecycler: RecyclerView
    private lateinit var recipeWeekRecycler: RecyclerView
    private lateinit var categoryRecycler: RecyclerView
    private lateinit var menuCard: MaterialCardView
    private lateinit var seeAll: TextView
    private lateinit var searchInput: EditText
    private lateinit var searchIcon: ImageView

    private val viewModel: HomeViewModel by viewModels {
        val tokenManager = TokenManager(this)
        val apiService = RetrofitClient.create(ApiService::class.java, tokenManager)
        HomeViewModelFactory(apiService)
    }

    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var recommendationAdapter: RecommendationAdapter
    private lateinit var recipeWeekAdapter: RecipeWeekAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        initViews()
        setupRecyclerViews()
        observeViewModel()
        
        viewModel.fetchData()
    }

    private fun initViews() {
        categoryRecycler = findViewById(R.id.categoryRecycler)
        recommendationRecycler = findViewById(R.id.recommendationRecycler)
        recipeWeekRecycler = findViewById(R.id.recipeOfWeekRecycler)
        menuCard = findViewById(R.id.menuCard)
        seeAll = findViewById(R.id.seeAll)
        searchInput = findViewById(R.id.searchInput)
        searchIcon = findViewById(R.id.searchIcon)

        menuCard.setOnClickListener {
            showPopupMenu()
        }

        searchInput.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                performSearch()
                true
            } else {
                false
            }
        }

        searchIcon.setOnClickListener {
            performSearch()
        }

        seeAll.setOnClickListener {
            val recipes = viewModel.recommendedRecipes.value
            if (recipes != null) {
                val intent = Intent(this, AllRecipesActivity::class.java)
                intent.putExtra("RECIPES_LIST", ArrayList(recipes))
                startActivity(intent)
            }
        }

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> true
                R.id.nav_search -> {
                    // Navigate to search results with empty list or specialized search page
                    val intent = Intent(this, AllRecipesActivity::class.java)
                    intent.putExtra("IS_SEARCH_ONLY", true)
                    startActivity(intent)
                    true
                }
                R.id.nav_saved -> {
                    startActivity(Intent(this, FavoriteRecipesActivity::class.java))
                    true
                }
                R.id.nav_profile -> {
                    startActivity(Intent(this, ProfileActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }

    private fun performSearch() {
        val query = searchInput.text.toString().trim()
        if (query.isNotEmpty()) {
            val intent = Intent(this, AllRecipesActivity::class.java)
            intent.putExtra("SEARCH_QUERY", query)
            startActivity(intent)
        } else {
            Toast.makeText(this, "Please enter a search query", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showPopupMenu() {
        val popupMenu = PopupMenu(this, menuCard, android.view.Gravity.END, 0, R.style.CustomPopupMenu)
        popupMenu.menuInflater.inflate(R.menu.menu_home, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.menu_logout -> {
                    logout()
                    true
                }
                else -> false
            }
        }
        popupMenu.show()
    }

    private fun logout() {
        lifecycleScope.launch {
            val tokenManager = TokenManager(this@HomeActivity)
            tokenManager.clearToken()
            
            // Navigate back to MainActivity (which handles routing to Login)
            val intent = Intent(this@HomeActivity, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }

    private fun setupRecyclerViews() {
        // Categories
        categoryAdapter = CategoryAdapter(emptyList()) { categoryName ->
            viewModel.filterByCategory(categoryName)
        }
        categoryRecycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        categoryRecycler.adapter = categoryAdapter

        // Recommendations
        recommendationAdapter = RecommendationAdapter(emptyList(), this)
        recommendationRecycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recommendationRecycler.adapter = recommendationAdapter

        // Recipe of the Week
        recipeWeekAdapter = RecipeWeekAdapter(emptyList(), this)
        recipeWeekRecycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recipeWeekRecycler.adapter = recipeWeekAdapter
    }

    private fun observeViewModel() {
        viewModel.categories.observe(this) { categories ->
            categoryAdapter.updateData(categories)
        }

        viewModel.recommendedRecipes.observe(this) { recipes ->
            recommendationAdapter.updateData(recipes)
        }

        viewModel.recipesOfWeek.observe(this) { recipes ->
            recipeWeekAdapter.updateData(recipes)
        }

        viewModel.errorMessage.observe(this) { message ->
            message?.let {
                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onItemClick(position: Int) {
        // Fallback or deprecated if onRecipeClick is used
    }

    override fun onRecipeClick(recipe: Recipe) {
        val intent = Intent(this, RecipeActivity::class.java)
        intent.putExtra("RECIPE_DATA", recipe)
        startActivity(intent)
    }
}
