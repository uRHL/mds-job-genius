package com.canonicalexamples.jobgenius.app

import android.app.Application
import com.canonicalexamples.jobgenius.model.Job
import com.canonicalexamples.jobgenius.model.JobDatabase
import com.canonicalexamples.jobgenius.model.JobFactsService
import com.google.gson.GsonBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class JobGeniusApp: Application() {
    val database by lazy { JobDatabase.getInstance(this) }
    val webservice by lazy {
        Retrofit.Builder()
            .baseUrl("https://jsonplaceholder.typicode.com/")
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build().create(JobFactsService::class.java)
    }
    override fun onCreate() {
        super.onCreate()

        CoroutineScope(Dispatchers.IO + SupervisorJob()).launch {
            database.clearAllTables()
            database.jobDao.apply {
                this.create(job = Job(name = "CocaCola", quantity = 1))
                this.create(job = Job(name = "Rice", quantity = 1))
                this.create(job = Job(name = "Bread", quantity = 1))
            }
        }
    }
}
