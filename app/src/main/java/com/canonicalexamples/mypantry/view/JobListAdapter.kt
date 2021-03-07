package com.canonicalexamples.mypantry.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.canonicalexamples.mypantry.databinding.ItemJobBinding
import com.canonicalexamples.mypantry.viewmodels.FoodsListViewModel


class JobListAdapter(private val viewModel: FoodsListViewModel): RecyclerView.Adapter<JobListAdapter.FoodItemViewHolder>() {

    class FoodItemViewHolder(private val viewModel: FoodsListViewModel, binding: ItemJobBinding): RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        val foodText = binding.foodText
        init {
            binding.root.setOnClickListener(this)
        }
        override fun onClick(v: View?) {
            viewModel.onClickItem(layoutPosition)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodItemViewHolder =
        FoodItemViewHolder(viewModel, ItemJobBinding.inflate(LayoutInflater.from(parent.context)))

    override fun onBindViewHolder(holder: FoodItemViewHolder, position: Int) {
        val food = viewModel.getItem(position)
        holder.foodText.text = "${food.name} quantity: ${food.quantity}"
    }

    override fun getItemCount(): Int = viewModel.numberOfItems
}
