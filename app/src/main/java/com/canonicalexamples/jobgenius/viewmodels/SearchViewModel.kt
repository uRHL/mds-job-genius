package com.canonicalexamples.jobgenius.viewmodels

import android.os.StrictMode
import androidx.lifecycle.*
import com.canonicalexamples.jobgenius.model.Job
import com.canonicalexamples.jobgenius.model.JobDatabase
import com.canonicalexamples.jobgenius.model.JobService
import com.canonicalexamples.jobgenius.util.Event
import com.google.gson.GsonBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class SearchViewModel(private val database: JobDatabase, private val webservice: JobService) : ViewModel() {
    private val _navigate: MutableLiveData<Event<Boolean>> = MutableLiveData()
    val navigate: LiveData<Event<Boolean>> = _navigate
    private var jobList = listOf<Job>()

    //data class JobCardShort(val title: String, val company: String, val remote: String, val fav: Boolean)
    //data class JobDetails(val title: String, val company: String, val remote: String, val fav: Boolean, val description: String, val logoUrl: String, val applyUrl: String  )

    init {
        viewModelScope.launch(Dispatchers.IO) {
            jobList = database.jobDao.fetchJob()
        }

    }


    suspend fun onClickSearch(searchFilters: String, isRemote: Boolean) {
        var location: String = ""
        if (isRemote){
            location = "Remote"
        }

        //we make the call
        val jobsCall: Call<List<Job>> = webservice.getJobs(1, searchFilters, location)
        val policy = StrictMode.ThreadPolicy.Builder()
                .permitAll().build()
        StrictMode.setThreadPolicy(policy)
        val jobs: List<Job>? = jobsCall.execute().body()
        //print(jobs)
        val listIterator = jobs?.listIterator()
        if (listIterator != null) {
            while (listIterator.hasNext()){
                var job: Job =listIterator.next()
                //print(job)
                if (job.id == null)
                    job.id = ""
                if (job.type == null)
                    job.type = ""
                if (job.url == null)
                    job.url = ""
                if (job.created_at == null)
                    job.created_at = ""
                if (job.company == null)
                    job.company = ""
                if (job.company_url == null)
                    job.company_url = ""
                if (job.location == null)
                    job.location = ""
                if (job.title == null)
                    job.title = ""
                if (job.description == null)
                    job.description = ""
                if (job.how_to_apply == null)
                    job.how_to_apply = ""
                if (job.company_logo == null)
                    job.company_logo = ""

                database.jobDao.create(job)
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
