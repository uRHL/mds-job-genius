package com.canonicalexamples.jobgenius.view

import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.canonicalexamples.jobgenius.R
import com.canonicalexamples.jobgenius.databinding.JobCardItemBinding
import com.canonicalexamples.jobgenius.model.job.Job
import com.canonicalexamples.jobgenius.viewmodels.JobListViewModel
import kotlinx.coroutines.runBlocking


class JobListAdapter(private val viewModel: JobListViewModel): RecyclerView.Adapter<JobListAdapter.JobItemViewHolder>() {

    private var jobList = listOf<Job>()

    class JobItemViewHolder(private val viewModel: JobListViewModel, val binding: JobCardItemBinding) : RecyclerView.ViewHolder(binding.root){
        var fav: Boolean = false
        init {
            // Set the listener on the "more" button
            binding.jobMore.setOnClickListener { viewModel.onClickJobMore(layoutPosition) }

            // Set the listener on the "favorite" button
            binding.jobFav.setOnClickListener {

                if(viewModel.isUserLogged()){
                    var ret = 0
                    runBlocking {
                        ret = if(!fav){
                            viewModel.saveFavJob(layoutPosition)
                        }else{
                            confirmFavJobDeletion(layoutPosition)
                        }
                    }
                    // Add/remove will return a positive Integer if the operation was successful
                    when (ret) {
                        1 -> switchFavouriteStatus()
                        -1 -> { Toast.makeText(binding.root.context, "It was not possible to save the job", Toast.LENGTH_LONG).show() }
                        else -> {}
                    }

                }else {
                    Toast.makeText(binding.root.context, "You have to be logged in", Toast.LENGTH_LONG).show()
                }
            }
        }

        private fun switchFavouriteStatus() {
            fav = !fav
            if(fav){
                // Job successfully saved as favorite
                binding.jobFav.setImageResource(R.drawable.ic_filled_heart)
                Toast.makeText(binding.root.context, "Job added to favorites", Toast.LENGTH_SHORT).show()
            }else{
                // Job successfully removed from favorites
                binding.jobFav.setImageResource(R.drawable.ic_empty_heart)
                Toast.makeText(binding.root.context, "Job removed from favorites", Toast.LENGTH_SHORT).show()
            }
        }

        private fun confirmFavJobDeletion(layoutPosition: Int): Int {
            var ret = 0
            val builder: AlertDialog.Builder = AlertDialog.Builder(binding.root.context)

            // Set the custom messages
            builder.setTitle("Confirm deletion")
            builder.setMessage("This job will be removed from favorites")

            // Set Cancelable false so if the user clicks outside the dialog it will be closed
            builder.setCancelable(false)

            // When the user click yes button the app will be closed
            builder.setPositiveButton("Yes") { dialog , which ->
                runBlocking { viewModel.removeFavJob(layoutPosition) }
                ret = 1
            }
            // When the user clicks "no" button the dialog is canceled
            builder.setNegativeButton("No" , DialogInterface.OnClickListener { dialog , which ->
                dialog.cancel()
            })

            // Create the Alert dialog and show it
            builder.create().show()
            return ret
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
