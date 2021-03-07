package com.canonicalexamples.jobgenius.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.canonicalexamples.jobgenius.databinding.ItemJobBinding
import com.canonicalexamples.jobgenius.viewmodels.JobListViewModel


class JobListAdapter(private val viewModel: JobListViewModel): RecyclerView.Adapter<JobListAdapter.JobItemViewHolder>() {

    class JobItemViewHolder(private val viewModel: JobListViewModel, binding: ItemJobBinding): RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        val jobText = binding.jobText
        init {
            binding.root.setOnClickListener(this)
        }
        override fun onClick(v: View?) {
            viewModel.onClickItem(layoutPosition)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobItemViewHolder =
        JobItemViewHolder(viewModel, ItemJobBinding.inflate(LayoutInflater.from(parent.context)))

    override fun onBindViewHolder(holder: JobItemViewHolder, position: Int) {
        val job = viewModel.getItem(position)
        holder.jobText.text = "${job.name} quantity: ${job.quantity}"
    }

    override fun getItemCount(): Int = viewModel.numberOfItems
}
