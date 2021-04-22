package com.canonicalexamples.jobgenius.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.canonicalexamples.jobgenius.databinding.JobCardItemBinding
import com.canonicalexamples.jobgenius.model.favoriteJob.FavoriteJob
import com.canonicalexamples.jobgenius.model.job.Job
import com.canonicalexamples.jobgenius.viewmodels.JobListViewModel


class FavoriteJobListAdapter(private val viewModel: JobListViewModel): RecyclerView.Adapter<FavoriteJobListAdapter.JobItemViewHolder>() {

    private var fabJobList = listOf<FavoriteJob>()

    class JobItemViewHolder(private val viewModel: JobListViewModel, val binding: JobCardItemBinding) : RecyclerView.ViewHolder(binding.root){

        init {
            // Set the listener on the "more" button
            binding.jobMore.setOnClickListener { viewModel.onClickJobMore(layoutPosition) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobItemViewHolder =
        JobItemViewHolder(viewModel, JobCardItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: JobItemViewHolder, position: Int) {

        val job = fabJobList[position]
        with(holder){
            binding.basicInfo.jobTitle.text = job.title
            binding.basicInfo.jobCompany.text = job.company
            binding.basicInfo.jobLocation.text = job.location
        }
    }

    override fun getItemCount(): Int = fabJobList.size

    fun setData(jobList: List<FavoriteJob>){
        this.fabJobList = jobList
        notifyDataSetChanged()
    }

}
