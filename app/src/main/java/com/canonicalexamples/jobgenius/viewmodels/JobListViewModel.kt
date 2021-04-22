package com.canonicalexamples.jobgenius.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.canonicalexamples.jobgenius.model.JobDatabase
import com.canonicalexamples.jobgenius.model.favoriteJob.FavoriteJob
import com.canonicalexamples.jobgenius.model.favoriteJob.FavoriteJobRepository
import com.canonicalexamples.jobgenius.model.job.Job
import com.canonicalexamples.jobgenius.model.job.JobRepository
import com.google.firebase.auth.FirebaseAuth


class JobListViewModel(private val database: JobDatabase, private val oauth: FirebaseAuth) : ViewModel() {

    private val jobRepository: JobRepository = JobRepository(database.jobDao())
    private val fabJobRepository: FavoriteJobRepository = FavoriteJobRepository(database.favoriteJobDao())
    val jobList: LiveData<List<Job>> = jobRepository.fetchJobs
    val fabJobList: LiveData<List<FavoriteJob>> = fabJobRepository.fetchFabJobs



    fun onClickJobMore(n: Int) {
        // Navigate to the fragment job details
        println("Item $n clicked")
        println("Some information: title ${jobList.value!![n].title}, description: ${jobList.value!![n].description}")
    }

    fun onClickJobFav(n: Int, status: Boolean) {
        // Navigate to the fragment job details

        if (status){
            // Insert into the database
            println("Item $n now added to favorite")
            val uid: String =  oauth.currentUser.uid
            println("******** UID = $uid")
//            val fabJob = FavoriteJob.parseJob(uid, jobList.value!![n])
//            runBlocking { fabJobRepository.addFabJob(fabJob) }

        }else {
            // Remove from the database
            println("Item $n now removed from favorites")
        }
    }
}

class JobListViewModelFactory(private val database: JobDatabase, private val oauth: FirebaseAuth) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(JobListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return JobListViewModel(database, oauth) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
