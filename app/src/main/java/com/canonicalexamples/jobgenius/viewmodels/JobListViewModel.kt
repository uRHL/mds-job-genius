package com.canonicalexamples.jobgenius.viewmodels

import androidx.lifecycle.*
import com.canonicalexamples.jobgenius.model.Job
import com.canonicalexamples.jobgenius.model.JobDatabase
import com.canonicalexamples.jobgenius.model.JobFactsService
import com.canonicalexamples.jobgenius.util.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class JobListViewModel(private val database: JobDatabase, private val webservice: JobFactsService) : ViewModel() {
    private val _navigate: MutableLiveData<Event<Boolean>> = MutableLiveData()
    val navigate: LiveData<Event<Boolean>> = _navigate
    private var jobList = listOf<Job>()

    data class Item(val name: String, val fav: Boolean)

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

    fun getItem(n: Int) = Item(name = jobList[n].title, fav = jobList[n].fav)

    fun onClickItem(n: Int) {
        println("Item $n clicked")
        //viewModelScope.launch(Dispatchers.IO) {
        //    val todo = webservice.getTodo(n).await()
        //    println("todo: ${todo.title}")
        // }
    }
}

class JobListViewModelFactory(private val database: JobDatabase, private val webservice: JobFactsService) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(JobListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return JobListViewModel(database, webservice) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
