package com.canonicalexamples.jobgenius.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.canonicalexamples.jobgenius.databinding.JobCardItemBinding
import com.canonicalexamples.jobgenius.model.Job
import com.canonicalexamples.jobgenius.viewmodels.JobListViewModel


class JobListAdapter(private val viewModel: JobListViewModel): RecyclerView.Adapter<JobListAdapter.JobItemViewHolder>() {

    private var jobList = listOf<Job>()

    class JobItemViewHolder(private val viewModel: JobListViewModel, binding: JobCardItemBinding): RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        val jobTitle = binding.basicInfo.jobTitle
        val jobCompany = binding.basicInfo.jobCompany
        val jobLocation = binding.basicInfo.jobLocation
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
        JobItemViewHolder(viewModel, JobCardItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: JobItemViewHolder, position: Int) {
        //val job = viewModel.getJobCard(position)
        val job = jobList[position]
        with(holder){
            jobTitle.text = "Title: " + job.title
            jobCompany.text = "Company: " + job.company
            holder.jobLocation.text = "Location: " + job.location
        }
    }

    override fun getItemCount(): Int = jobList.size

    fun setData(jobList: List<Job>){
        this.jobList = jobList
        notifyDataSetChanged()
    }
}
