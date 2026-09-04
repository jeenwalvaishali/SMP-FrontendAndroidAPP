package com.example.smartmealplanner.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.smartmealplanner.R
import com.example.smartmealplanner.data.model.Category
import com.google.android.material.card.MaterialCardView

class CategoryAdapter(
    private var categories: List<Category>,
    private val onCategorySelected: (String) -> Unit
) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    private var selectedPosition = 0

    class CategoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.categoryName)
        val icon: ImageView = view.findViewById(R.id.categoryIcon)
        val card: MaterialCardView = view.findViewById(R.id.categoryCard)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_category, parent, false)
        return CategoryViewHolder(view)
    }

    fun updateData(newCategories: List<Category>) {
        this.categories = newCategories
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categories[position]
        holder.name.text = category.name

        if (category.imageUrl == null) {
            holder.icon.visibility = View.GONE
        } else {
            holder.icon.visibility = View.VISIBLE
            Glide.with(holder.itemView.context)
                .load(category.imageUrl)
                .into(holder.icon)
        }

        if (position == selectedPosition) {
            holder.card.setCardBackgroundColor(Color.parseColor("#064420"))
            holder.name.setTextColor(Color.WHITE)
        } else {
            holder.card.setCardBackgroundColor(Color.WHITE)
            holder.name.setTextColor(Color.parseColor("#333333"))
        }

        holder.itemView.setOnClickListener {
            val lastSelected = selectedPosition
            selectedPosition = holder.bindingAdapterPosition
            notifyItemChanged(lastSelected)
            notifyItemChanged(selectedPosition)
            onCategorySelected(category.name)
        }
    }

    override fun getItemCount() = categories.size
}