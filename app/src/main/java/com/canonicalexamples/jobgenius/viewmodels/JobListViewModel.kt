package com.canonicalexamples.jobgenius.viewmodels

import androidx.lifecycle.*
import com.canonicalexamples.jobgenius.model.Job
import com.canonicalexamples.jobgenius.model.JobDatabase
import com.canonicalexamples.jobgenius.model.JobRepository
import com.canonicalexamples.jobgenius.model.JobService
import com.canonicalexamples.jobgenius.util.Event


class JobListViewModel(private val database: JobDatabase, private val webservice: JobService) : ViewModel() {
//    private val _navigate: MutableLiveData<Event<Boolean>> = MutableLiveData()
//    val navigate: LiveData<Event<Boolean>> = _navigate
    private val repository: JobRepository = JobRepository(database.jobDao())
    val jobList: LiveData<List<Job>> = repository.fetchJobs

    data class JobCardShort(val title: String, val company: String, val remote: String, val fav: Boolean)
    data class JobDetails(val title: String, val company: String, val remote: String, val fav: Boolean, val description: String, val logoUrl: String, val applyUrl: String  )


//    fun addButtonClicked() {
//        _navigate.value = Event(true)
//    }

    fun getJobCard(n: Int): JobCardShort? {
        if (jobList.value != null || n >= 0 || n < jobList.value!!.size){
            return JobCardShort(
                    jobList.value!![n].title,
                    jobList.value!![n].company,
                    jobList.value!![n].location,
                    jobList.value!![n].fav)
        }
        return null
    }

    fun onClickJobMore(n: Int) {
        // Navigate to the fragment job details
        println("Item $n clicked")
        println("Some information: title ${jobList.value!![n].title}, description: ${jobList.value!![n].description}")
    }
}

class JobListViewModelFactory(private val database: JobDatabase, private val webservice: JobService) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(JobListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return JobListViewModel(database, webservice) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
