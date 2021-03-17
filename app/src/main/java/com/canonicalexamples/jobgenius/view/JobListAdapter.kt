package com.canonicalexamples.jobgenius.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.canonicalexamples.jobgenius.databinding.JobCardItemBinding
import com.canonicalexamples.jobgenius.viewmodels.JobListViewModel


class JobListAdapter(private val viewModel: JobListViewModel): RecyclerView.Adapter<JobListAdapter.JobItemViewHolder>() {

    class JobItemViewHolder(private val viewModel: JobListViewModel, binding: JobCardItemBinding): RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        val jobTitle = binding.basicInfo.jobTitle
        val jobCompany = binding.basicInfo.jobCompany
        val jobType = binding.basicInfo.jobRemote
        val fav = binding.jobFav
        init {
            // Set the listener on the "more" button
            binding.jobMore.setOnClickListener(this)
        }
        override fun onClick(v: View?) {
            viewModel.onClickJobMore(layoutPosition)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobItemViewHolder =
        JobItemViewHolder(viewModel, JobCardItemBinding.inflate(LayoutInflater.from(parent.context)))

    override fun onBindViewHolder(holder: JobItemViewHolder, position: Int) {
        val job = viewModel.getJobCard(position)
        holder.jobTitle.text = job.title
        holder.jobCompany.text = job.company
        if (job.remote) {
            holder.jobType.text = "Remote"
        }else {
            holder.jobType.text = "Not remote"
        }
        //holder.fav.background =
    }

    override fun getItemCount(): Int = viewModel.numberOfItems
}
