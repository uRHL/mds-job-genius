package com.canonicalexamples.jobgenius.viewmodels

import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.*
import com.canonicalexamples.jobgenius.app.JobGeniusApp
import com.canonicalexamples.jobgenius.model.job.Job
import com.canonicalexamples.jobgenius.model.JobDatabase
import com.canonicalexamples.jobgenius.model.job.JobRepository
import com.canonicalexamples.jobgenius.model.JobService
import com.canonicalexamples.jobgenius.model.favoriteJob.FavoriteJob
import com.canonicalexamples.jobgenius.model.favoriteJob.FavoriteJobRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.runBlocking


class JobListViewModel(private val database: JobDatabase, private val oauth: FirebaseAuth) : ViewModel() {
//    private val _navigate: MutableLiveData<Event<Boolean>> = MutableLiveData()
//    val navigate: LiveData<Event<Boolean>> = _navigate
    private val jobRepository: JobRepository = JobRepository(database.jobDao())
    private val fabJobRepository: FavoriteJobRepository = FavoriteJobRepository(database.favoriteJobDao())
    val jobList: LiveData<List<Job>> = jobRepository.fetchJobs
    val fabJobList: LiveData<List<FavoriteJob>> = fabJobRepository.fetchFabJobs

//    data class JobCardShort(val title: String, val company: String, val location: String, val fav: Boolean)
//    data class JobDetails(val title: String, val company: String, val location: String, val fav: Boolean, val description: String, val logoUrl: String, val applyUrl: String  )


//    fun addButtonClicked() {
//        _navigate.value = Event(true)
//    }

//    fun getJobCard(n: Int): JobCardShort? {
//        if (jobList.value != null || n >= 0 || n < jobList.value!!.size){
//            return JobCardShort(
//                    jobList.value!![n].title,
//                    jobList.value!![n].company,
//                    jobList.value!![n].location,
//                    jobList.value!![n].fav)
//        }
//        return null
//    }

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

//        println("Some information: title ${jobList.value!![n].title}, description: ${jobList.value!![n].description}")
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
