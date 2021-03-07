package com.canonicalexamples.mypantry.model

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface JobFactsService {
    @GET("/positions.json")
    fun getJobs(): Call<JobFacts>

    @GET("/positions.json?location=Remote")
    fun getRemoteJobs(): Call<JobFacts>


}
