package com.canonicalexamples.jobgenius.viewmodels

import android.os.StrictMode
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.canonicalexamples.jobgenius.model.JobDatabase
import com.canonicalexamples.jobgenius.model.JobService
import com.canonicalexamples.jobgenius.model.job.Job
import retrofit2.Call


class SearchViewModel(private val database: JobDatabase, private val webservice: JobService) : ViewModel() {

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

        // Empty the table
        database.jobDao().dropTable()

        val listIterator = jobs?.listIterator()
        if (listIterator != null) {
            while (listIterator.hasNext()){
                var job: Job =listIterator.next()
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
