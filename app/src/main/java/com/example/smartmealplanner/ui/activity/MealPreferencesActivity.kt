package com.example.smartmealplanner.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.smartmealplanner.R
import com.example.smartmealplanner.data.api.MealPlanApiService
import com.example.smartmealplanner.data.api.RetrofitClient
import com.example.smartmealplanner.data.api.TokenManager
import com.example.smartmealplanner.data.model.MealPlanRequest
import com.example.smartmealplanner.data.repository.MealPlanRepository
import com.example.smartmealplanner.ui.viewmodel.MealPlanState
import com.example.smartmealplanner.ui.viewmodel.MealPlanViewModel
import com.example.smartmealplanner.ui.viewmodel.MealPlanViewModelFactory
import kotlinx.coroutines.launch

class MealPreferencesActivity : AppCompatActivity() {

    private lateinit var dietSpinner: Spinner
    private lateinit var caloriesInput: EditText
    private lateinit var mealsPerDaySpinner: Spinner
    private lateinit var cuisineSpinner: Spinner
    private lateinit var cookingTimeSpinner: Spinner
    private lateinit var generateButton: Button

    private val viewModel: MealPlanViewModel by viewModels {
        val tokenManager = TokenManager(this)
        val apiService = RetrofitClient.create(MealPlanApiService::class.java, tokenManager)
        val repository = MealPlanRepository(apiService)
        MealPlanViewModelFactory(repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meal_preferences)

        initViews()
        setupSpinners()
        observeViewModel()
    }

    private fun initViews() {
        dietSpinner = findViewById(R.id.dietSpinner)
        caloriesInput = findViewById(R.id.caloriesInput)
        mealsPerDaySpinner = findViewById(R.id.mealsPerDaySpinner)
        cuisineSpinner = findViewById(R.id.cuisineSpinner)
        cookingTimeSpinner = findViewById(R.id.cookingTimeSpinner)
        generateButton = findViewById(R.id.generateButton)
        
        generateButton.setOnClickListener {
            val caloriesText = caloriesInput.text.toString()
            if (caloriesText.isEmpty()) {
                Toast.makeText(this, "Please enter daily calories", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            
            val calories = caloriesText.toIntOrNull() ?: 2000
            val cookingTimeStr = cookingTimeSpinner.selectedItem.toString()
            val maxCookingTime = when {
                cookingTimeStr.contains("30") -> 30
                cookingTimeStr.contains("60") -> 60
                else -> 120
            }


            // Convert strings to lowercase for backend compatibility
            val request = MealPlanRequest(
                dietType = dietSpinner.selectedItem.toString(),
                dailyCalories = calories,
                mealsPerDay = mealsPerDaySpinner.selectedItem.toString().toInt(),
                cuisine = cuisineSpinner.selectedItem.toString(),
                maxCookingTime = maxCookingTime
            )

            // 🔍 LOG THE VALUES HERE
            Log.d("MealPlanRequest", """
            Diet Type: ${request.dietType}
            Daily Calories: ${request.dailyCalories}
            Meals Per Day: ${request.mealsPerDay}
            Cuisine: ${request.cuisine}
            Max Cooking Time: ${request.maxCookingTime}
        """.trimIndent())

            viewModel.generateMealPlan(request)
        }
    }

    private fun setupSpinners() {
        val diets = arrayOf("Vegetarian", "Vegan", "Non-Vegetarian", "Keto", "Paleo")
        dietSpinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, diets)

        val meals = arrayOf("2", "3", "4", "5")
        mealsPerDaySpinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, meals)

        val cuisines = arrayOf("Indian", "Italian", "Mexican", "Chinese", "Continental")
        cuisineSpinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, cuisines)

        val times = arrayOf("Less than 30 min", "30-60 min", "More than 60 min")
        cookingTimeSpinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, times)
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.navigationEvent.collect { plan ->
                        val intent = Intent(this@MealPreferencesActivity, WeeklyMealPlanActivity::class.java)
                        intent.putExtra("WEEKLY_PLAN", plan)
                        startActivity(intent)
                    }
                }

                launch {
                    viewModel.uiState.collect { state ->
                        when (state) {
                            is MealPlanState.Loading -> {
                                generateButton.isEnabled = false
                                generateButton.text = "Generating..."
                            }
                            is MealPlanState.Success -> {
                                generateButton.isEnabled = true
                                generateButton.text = "Generate Meal Plan"
                            }
                            is MealPlanState.Error -> {
                                generateButton.isEnabled = true
                                generateButton.text = "Generate Meal Plan"
                                // Displaying the specific error from the backend if possible
                                Toast.makeText(this@MealPreferencesActivity, state.message, Toast.LENGTH_LONG).show()
                            }
                            null -> {
                                generateButton.isEnabled = true
                                generateButton.text = "Generate Meal Plan"
                            }
                        }
                    }
                }
            }
        }
    }
}
