package com.canonicalexamples.mypantry.app

import android.app.Application
import com.canonicalexamples.mypantry.model.Food
import com.canonicalexamples.mypantry.model.FoodDatabase
import com.canonicalexamples.mypantry.model.FoodFactsService
import com.google.gson.GsonBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MyPantryApp: Application() {
    val database by lazy { FoodDatabase.getInstance(this) }
    val webservice by lazy {
        Retrofit.Builder()
            .baseUrl("https://jsonplaceholder.typicode.com/")
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build().create(FoodFactsService::class.java)
    }
    override fun onCreate() {
        super.onCreate()

        CoroutineScope(Dispatchers.IO + SupervisorJob()).launch {
            database.clearAllTables()
            database.foodDao.apply {
                this.create(food = Food(name = "CocaCola", quantity = 1))
                this.create(food = Food(name = "Rice", quantity = 1))
                this.create(food = Food(name = "Bread", quantity = 1))
            }
        }
    }
}
