package com.example.smartmealplanner.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.smartmealplanner.R
import com.example.smartmealplanner.data.model.DayPlan
import com.example.smartmealplanner.data.model.Meal

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
        holder.bind(dayPlans[position])
    }

    override fun getItemCount(): Int = dayPlans.size

    class MealPlanViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val dayTitle: TextView = itemView.findViewById(R.id.dayTitle)
        private val totalDayCalories: TextView = itemView.findViewById(R.id.totalDayCalories)
        private val mealsContainer: LinearLayout = itemView.findViewById(R.id.mealsContainer)

        fun bind(plan: DayPlan) {
            dayTitle.text = plan.day.uppercase()
            totalDayCalories.text = "${plan.totalCalories} kcal"
            
            mealsContainer.removeAllViews()
            val inflater = LayoutInflater.from(itemView.context)
            
            plan.meals.forEach { meal ->
                val mealView = inflater.inflate(R.layout.item_meal_detail, mealsContainer, false)
                
                val typeTv = mealView.findViewById<TextView>(R.id.mealTypeTv)
                val titleTv = mealView.findViewById<TextView>(R.id.recipeTitleTv)
                val infoTv = mealView.findViewById<TextView>(R.id.recipeInfoTv)
                val image = mealView.findViewById<ImageView>(R.id.recipeImage)
                val ingredientsTv = mealView.findViewById<TextView>(R.id.ingredientsTv)
                val stepsTv = mealView.findViewById<TextView>(R.id.stepsTv)

                typeTv.text = meal.mealType
                titleTv.text = meal.recipe.title
                infoTv.text = "${meal.recipe.calories ?: 0} kcal | ${meal.recipe.prepTime} mins"
                
                ingredientsTv.text = "Ingredients: ${meal.recipe.ingredients.joinToString(", ")}"
                stepsTv.text = "Steps:\n${meal.recipe.steps.mapIndexed { i, s -> "${i + 1}. $s" }.joinToString("\n")}"

                Glide.with(itemView.context)
                    .load(meal.recipe.imageUrl)
                    .placeholder(R.drawable.ic_launcher_background)
                    .into(image)

                mealsContainer.addView(mealView)
            }
        }
    }
}
