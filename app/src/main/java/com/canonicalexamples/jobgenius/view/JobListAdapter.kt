package com.canonicalexamples.jobgenius.view

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.canonicalexamples.jobgenius.R
import com.canonicalexamples.jobgenius.databinding.JobCardItemBinding
import com.canonicalexamples.jobgenius.model.favoriteJob.FavoriteJob
import com.canonicalexamples.jobgenius.model.job.Job
import com.canonicalexamples.jobgenius.viewmodels.JobListViewModel


class JobListAdapter(private val viewModel: JobListViewModel): RecyclerView.Adapter<JobListAdapter.JobItemViewHolder>() {

    private var jobList = listOf<Job>()

    class JobItemViewHolder(private val viewModel: JobListViewModel, val binding: JobCardItemBinding) : RecyclerView.ViewHolder(binding.root){
        var fav: Boolean = false
        init {
            // Set the listener on the "more" button
            binding.jobMore.setOnClickListener { viewModel.onClickJobMore(layoutPosition) }

            // Set the listener on the "favorite" button
            binding.jobFav.setOnClickListener {

                // Swap the favorite state
                fav = !fav

                viewModel.onClickJobFav(layoutPosition, fav)

                // Update the UI according to the changes
                if(fav){
                    binding.jobFav.setImageResource(R.drawable.ic_filled_heart)
                    Toast.makeText(binding.root.context, "Job added to favorites", Toast.LENGTH_LONG).show()
                }else {
                    binding.jobFav.setImageResource(R.drawable.ic_empty_heart)
                    Toast.makeText(binding.root.context, "Job removed from favorites", Toast.LENGTH_LONG).show()
                }


            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobItemViewHolder =
        JobItemViewHolder(viewModel, JobCardItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: JobItemViewHolder, position: Int) {

        val job = jobList[position]
        with(holder){
            binding.basicInfo.jobTitle.text = job.title
            binding.basicInfo.jobCompany.text = job.company
            binding.basicInfo.jobLocation.text = job.location
        }
    }

    override fun getItemCount(): Int = jobList.size

    fun setData(jobList: List<Job>){
        this.jobList = jobList
        notifyDataSetChanged()
    }

}
