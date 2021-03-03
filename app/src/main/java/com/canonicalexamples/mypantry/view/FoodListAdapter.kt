package com.canonicalexamples.mypantry.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.canonicalexamples.mypantry.databinding.ItemTeaBinding
import com.canonicalexamples.mypantry.viewmodels.TeasListViewModel


class FoodListAdapter(private val viewModel: TeasListViewModel): RecyclerView.Adapter<FoodListAdapter.FoodItemViewHolder>() {

    class FoodItemViewHolder(private val viewModel: TeasListViewModel, binding: ItemTeaBinding): RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        val teaName = binding.teaName
        init {
            binding.root.setOnClickListener(this)
        }
        override fun onClick(v: View?) {
            viewModel.onClickItem(layoutPosition)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodItemViewHolder =
        FoodItemViewHolder(viewModel, ItemTeaBinding.inflate(LayoutInflater.from(parent.context)))

    override fun onBindViewHolder(holder: FoodItemViewHolder, position: Int) {
        val tea = viewModel.getItem(position)
        holder.teaName.text = "Wonderful ${tea.name}"
    }

    override fun getItemCount(): Int = viewModel.numberOfItems
}
