package com.example.smartmealplanner.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smartmealplanner.R
import com.example.smartmealplanner.adapter.MealPlanAdapter
import com.example.smartmealplanner.data.model.MealPlanResponse

class WeeklyMealPlanActivity : AppCompatActivity() {

    private lateinit var weeklyPlanRecycler: RecyclerView
    private lateinit var adapter: MealPlanAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weekly_meal_plan)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { finish() }

        weeklyPlanRecycler = findViewById(R.id.weeklyPlanRecycler)
        weeklyPlanRecycler.layoutManager = LinearLayoutManager(this)

        // Using the new MealPlanResponse model
        val mealPlan = intent.getSerializableExtra("WEEKLY_PLAN") as? MealPlanResponse
        
        val dayPlans = mealPlan?.days ?: emptyList()

        adapter = MealPlanAdapter(dayPlans)
        weeklyPlanRecycler.adapter = adapter
    }
}
