package com.canonicalexamples.jobgenius.app

import android.app.Application
import android.content.Context
import com.canonicalexamples.jobgenius.model.JobDatabase
import com.canonicalexamples.jobgenius.model.JobService
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
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
                .baseUrl("https://jobs.github.com/positions.json/")
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
                .build().create(JobService::class.java)
    }

    val auth : FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    override fun onCreate() {
        super.onCreate()

        CoroutineScope(Dispatchers.IO + SupervisorJob()).launch {
            database.clearAllTables()
        }
    }
}
