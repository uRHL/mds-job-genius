package com.canonicalexamples.mypantry.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.canonicalexamples.mypantry.model.Food
import com.canonicalexamples.mypantry.model.FoodDatabase
import com.canonicalexamples.mypantry.model.FoodFactsService
import com.canonicalexamples.mypantry.util.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.await


class TeasListViewModel(private val database: FoodDatabase, private val webservice: FoodFactsService): ViewModel() {
    private val _navigate: MutableLiveData<Event<Boolean>> = MutableLiveData()
    val navigate: LiveData<Event<Boolean>> = _navigate
    private var teasList = listOf<Food>()
    data class Item(val name: String)

    init {
        viewModelScope.launch(Dispatchers.IO) {
            teasList = database.foodDao.fetchTeas()
        }
    }

    val numberOfItems: Int
        get() = teasList.count()

    fun addButtonClicked() {
        _navigate.value = Event(true)
    }

    fun getItem(n: Int) = Item(name = teasList[n].name)

    fun onClickItem(n: Int) {
        println("Item $n clicked")
        viewModelScope.launch(Dispatchers.IO) {
            val todo = webservice.getTodo(n).await()
            println("todo: ${todo.title}")
        }
    }
}

class TeasListViewModelFactory(private val database: FoodDatabase, private val webservice: FoodFactsService): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TeasListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TeasListViewModel(database, webservice) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
