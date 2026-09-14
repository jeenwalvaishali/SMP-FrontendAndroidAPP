package com.example.smartmealplanner.ui.activity

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smartmealplanner.R
import com.example.smartmealplanner.adapter.MealPlanAdapter
import com.example.smartmealplanner.data.api.MealPlanApiService
import com.example.smartmealplanner.data.api.RetrofitClient
import com.example.smartmealplanner.data.api.TokenManager
import com.example.smartmealplanner.data.repository.MealPlanRepository
import com.example.smartmealplanner.ui.viewmodel.MealPlanState
import com.example.smartmealplanner.ui.viewmodel.MealPlanViewModel
import com.example.smartmealplanner.ui.viewmodel.MealPlanViewModelFactory
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class SavedMealPlanActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var weekStartDateTv: TextView
    private lateinit var preferencesTv: TextView
    private lateinit var daysRecyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var errorLayout: LinearLayout
    private lateinit var errorTv: TextView
    private lateinit var retryButton: Button

    private lateinit var adapter: MealPlanAdapter

    private val viewModel: MealPlanViewModel by viewModels {
        val tokenManager = TokenManager(this)
        val apiService = RetrofitClient.create(MealPlanApiService::class.java, tokenManager)
        val repository = MealPlanRepository(apiService)
        MealPlanViewModelFactory(repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saved_meal_plan)

        initViews()
        setupRecyclerView()
        observeViewModel()

        viewModel.getSavedMealPlan()
    }

    private fun initViews() {
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressed() }

        weekStartDateTv = findViewById(R.id.weekStartDateTv)
        preferencesTv = findViewById(R.id.preferencesTv)
        daysRecyclerView = findViewById(R.id.daysRecyclerView)
        progressBar = findViewById(R.id.progressBar)
        errorLayout = findViewById(R.id.errorLayout)
        errorTv = findViewById(R.id.errorTv)
        retryButton = findViewById(R.id.retryButton)

        retryButton.setOnClickListener {
            viewModel.getSavedMealPlan()
        }
    }

    private fun setupRecyclerView() {
        adapter = MealPlanAdapter(emptyList())
        daysRecyclerView.layoutManager = LinearLayoutManager(this)
        daysRecyclerView.adapter = adapter
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    when (state) {
                        is MealPlanState.Loading -> {
                            progressBar.visibility = View.VISIBLE
                            errorLayout.visibility = View.GONE
                        }
                        is MealPlanState.SavedPlanSuccess -> {
                            progressBar.visibility = View.GONE
                            errorLayout.visibility = View.GONE
                            val plan = state.savedPlan
                            
                            // Format date
                            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
                            inputFormat.timeZone = TimeZone.getTimeZone("UTC")
                            val outputFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
                            try {
                                val date = inputFormat.parse(plan.weekStartDate)
                                weekStartDateTv.text = "Week Starting: ${outputFormat.format(date)}"
                            } catch (e: Exception) {
                                weekStartDateTv.text = "Week Starting: ${plan.weekStartDate}"
                            }

                            val prefs = plan.preferences
                            preferencesTv.text = "Preferences: ${prefs.dietType}, ${prefs.cuisine}, ${prefs.dailyCalories} kcal, ${prefs.mealsPerDay} meals/day, ${prefs.maxCookingTime} min"
                            
                            adapter.updateData(plan.days)
                        }
                        is MealPlanState.Error -> {
                            progressBar.visibility = View.GONE
                            errorLayout.visibility = View.VISIBLE
                            
                            val message = when (state.code) {
                                401 -> "Unauthorized. Please login again."
                                404 -> "No saved meal plan found for this week."
                                500 -> "Server error. Please try again later."
                                else -> state.message
                            }
                            errorTv.text = message
                        }
                        else -> {}
                    }
                }
            }
        }
    }
}
