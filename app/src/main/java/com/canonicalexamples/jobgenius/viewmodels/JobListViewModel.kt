package com.canonicalexamples.jobgenius.viewmodels

import androidx.lifecycle.*
import com.canonicalexamples.jobgenius.model.Job
import com.canonicalexamples.jobgenius.model.JobDatabase
import com.canonicalexamples.jobgenius.model.JobService
import com.canonicalexamples.jobgenius.util.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class JobListViewModel(private val database: JobDatabase, private val webservice: JobService) : ViewModel() {
    private val _navigate: MutableLiveData<Event<Boolean>> = MutableLiveData()
    val navigate: LiveData<Event<Boolean>> = _navigate
    private var jobList = listOf<Job>()

    data class JobCardShort(val title: String, val company: String, val remote: String, val fav: Boolean)
    data class JobDetails(val title: String, val company: String, val remote: String, val fav: Boolean, val description: String, val logoUrl: String, val applyUrl: String  )

    init {
        viewModelScope.launch(Dispatchers.IO) {
            jobList = database.jobDao.fetchJob()
        }
    }

    val numberOfItems: Int
        get() = jobList.count()

    fun addButtonClicked() {
        _navigate.value = Event(true)
    }

    fun getJobCard(n: Int) = JobCardShort(jobList[n].title, jobList[n].company, jobList[n].location, jobList[n].fav)

    fun onClickJobMore(n: Int) {
        println("Item $n clicked")

        // Navigate to the fragment job details
        println("Some information: title ${jobList[n].title}, description: ${jobList[n].description}")

        //println("Pues este es su texto: $searchFilters\nY este su remote: $isRemote")
        //viewModelScope.launch(Dispatchers.IO) {
        //    val todo = webservice.getTodo(n).await()
        //    println("todo: ${todo.title}")
        // }
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
