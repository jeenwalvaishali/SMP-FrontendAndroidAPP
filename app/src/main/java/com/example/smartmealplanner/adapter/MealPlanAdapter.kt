package com.example.smartmealplanner.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.smartmealplanner.R
import com.example.smartmealplanner.data.model.DayPlan

class MealPlanAdapter(private var dayPlans: List<DayPlan>) :
    RecyclerView.Adapter<MealPlanAdapter.MealPlanViewHolder>() {

    fun updateData(newDayPlans: List<DayPlan>) {
        this.dayPlans = newDayPlans
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealPlanViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_day_meal_plan, parent, false)
        return MealPlanViewHolder(view)
    }

    override fun onBindViewHolder(holder: MealPlanViewHolder, position: Int) {
        val plan = dayPlans[position]
        holder.bind(plan)
    }

    override fun getItemCount(): Int = dayPlans.size

    class MealPlanViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val dayTitle: TextView = itemView.findViewById(R.id.dayTitle)
        private val breakfastTitle: TextView = itemView.findViewById(R.id.breakfastTitle)
        private val breakfastCalories: TextView = itemView.findViewById(R.id.breakfastCalories)
        private val lunchTitle: TextView = itemView.findViewById(R.id.lunchTitle)
        private val lunchCalories: TextView = itemView.findViewById(R.id.lunchCalories)
        private val dinnerTitle: TextView = itemView.findViewById(R.id.dinnerTitle)
        private val dinnerCalories: TextView = itemView.findViewById(R.id.dinnerCalories)
        private val totalDayCalories: TextView = itemView.findViewById(R.id.totalDayCalories)

        fun bind(plan: DayPlan) {
            dayTitle.text = plan.day.uppercase()
            
            // Assuming meals are in order: Breakfast, Lunch, Dinner
            val breakfast = plan.meals.find { it.mealType.lowercase() == "breakfast" }
            val lunch = plan.meals.find { it.mealType.lowercase() == "lunch" }
            val dinner = plan.meals.find { it.mealType.lowercase() == "dinner" }

            breakfast?.let {
                breakfastTitle.text = it.recipe.title
                breakfastCalories.text = "Calories: ${it.recipe.prepTime}" // Using prepTime as placeholder or if Recipe has calories
            }
            
            lunch?.let {
                lunchTitle.text = it.recipe.title
                lunchCalories.text = "Calories: ${it.recipe.prepTime}"
            }
            
            dinner?.let {
                dinnerTitle.text = it.recipe.title
                dinnerCalories.text = "Calories: ${it.recipe.prepTime}"
            }
            
            totalDayCalories.text = "${plan.totalCalories} Calories"
        }
    }
}
