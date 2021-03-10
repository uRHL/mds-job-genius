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
                this.create(job = Job(0, githubId = "85f49b29-5845-4154-bf1a-5bf842419f1e", title = "Senior DevOps Engineer", fav = false, company = "Wolfe.com LLC", location = "Pittsburgh Pa", description = "We’re looking for a strong engineer to:</p>\\n<ul>\\n<li>Own the AWS infrastructure of our 3 core properties, including Dockerized ECS and more standard EC2 based systems, while working with a small team", imageUrl = "https://jobs.github.com/rails/active_storage/blobs/eyJfcmFpbHMiOnsibWVzc2FnZSI6IkJBaHBBdkdiIiwiZXhwIjpudWxsLCJwdXIiOiJibG9iX2lkIn19--ad77badc79a2c42a15179df02e3b0387c666128f/wolfe.png", url = "https://jobs.github.com/positions/85f49b29-5845-4154-bf1a-5bf842419f1e"))
                this.create(job = Job(0, githubId = "6e597f81-3cbb-46d6-b8d9-89c9b6a34f0f", title = "Senior Technical Product Owner", fav = false, company = "DISH", location = "Englewood, CO", description = "At DISH, our Retail Wireless Integration teams challenge the status quo and reimagine capabilities across industries. From e-commerce platform to other BSS and OSS systems, to big data and beyond, our team play vital roles in connecting consumers with the products and platforms of tomorrow.", imageUrl = "https://jobs.github.com/rails/active_storage/blobs/eyJfcmFpbHMiOnsibWVzc2FnZSI6IkJBaHBBdU9iIiwiZXhwIjpudWxsLCJwdXIiOiJibG9iX2lkIn19--66ae4c0a6fb1058015a93d4bb035ec9802bf4c4c/DISH_WORDMARK_RED_LOGO_1115818_RGB_Newsroom.png", url = "https://jobs.github.com/positions/6e597f81-3cbb-46d6-b8d9-89c9b6a34f0f"))
                this.create(job = Job(0, githubId = "f4cce294-f946-4fbe-800d-3e213a60b314", title = "IT Specialist", fav = false, company = "Huew Magoo's LLC", location = "East Coast", description = "<p>The IT Specialist is responsible for managing, supporting, overseeing, and enhancing the company’s data center, cloud, corporate, and distributed network infrastructure. The IT Specialist will design, implement, monitor and support local and wide area networks as well the restaurants various technologies VoIP, Firewalls, Wi-Fi and other technologies, reporting, and data analytics routine maintenance of databases. ", imageUrl = "https://jobs.github.com/rails/active_storage/blobs/eyJfcmFpbHMiOnsibWVzc2FnZSI6IkJBaHBBcitiIiwiZXhwIjpudWxsLCJwdXIiOiJibG9iX2lkIn19--06239d27ad5337034d46f3f0da989d70e428eb61/Huey%20Magoo's%20Logo.jpg", url = "https://jobs.github.com/positions/f4cce294-f946-4fbe-800d-3e213a60b314"))
            }
        }
    }
}
