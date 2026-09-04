package com.example.smartmealplanner.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide


import com.example.smartmealplanner.R
import com.example.smartmealplanner.data.model.Recipe
import com.example.smartmealplanner.databinding.ItemFavoriteBinding

class FavoriteRecipeAdapter(private val list: List<Recipe>) :
    RecyclerView.Adapter<FavoriteRecipeAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemFavoriteBinding) :
        RecyclerView.ViewHolder(binding.root)

    var isExpanded = false
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemFavoriteBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount() : Int {
        return if (isExpanded || list.size <= 4) {
            list.size
        } else {
            4 // Show only first 4 items when collapsed
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.binding.tvRecipeTitle.text = item.title
        holder.binding.tvRecipeDesc.text = item.description
        
        Glide.with(holder.binding.ivRecipe.context)
            .load(item.imageUrl)
            .placeholder(R.drawable.sample_soup)
            .error(R.drawable.sample_soup)
            .into(holder.binding.ivRecipe)
    }
}