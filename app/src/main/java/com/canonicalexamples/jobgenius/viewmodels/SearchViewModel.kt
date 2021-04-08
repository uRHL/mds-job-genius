package com.canonicalexamples.jobgenius.viewmodels

import android.os.StrictMode
import androidx.lifecycle.*
import com.canonicalexamples.jobgenius.model.Job
import com.canonicalexamples.jobgenius.model.JobDatabase
import com.canonicalexamples.jobgenius.model.JobService
import com.canonicalexamples.jobgenius.util.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call


class SearchViewModel(private val database: JobDatabase, private val webservice: JobService) : ViewModel() {
    private val _navigate: MutableLiveData<Event<Boolean>> = MutableLiveData()
    val navigate: LiveData<Event<Boolean>> = _navigate

    suspend fun onClickSearch(searchFilters: String, isRemote: Boolean) {
        var location = ""
        if (isRemote){
            location = "Remote"
        }

        //we make the call
        val jobsCall: Call<List<Job>> = webservice.getJobs(1, searchFilters, location)
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        val jobs: List<Job>? = jobsCall.execute().body()
        //print(jobs)
        val listIterator = jobs?.listIterator()
        if (listIterator != null) {
            while (listIterator.hasNext()){
                var job: Job =listIterator.next()
                //print(job)
                database.jobDao().addJob(job)
            }
        }
    }
}

class SearchViewModelFactory(private val database: JobDatabase, private val webservice: JobService) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SearchViewModel(database, webservice) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
