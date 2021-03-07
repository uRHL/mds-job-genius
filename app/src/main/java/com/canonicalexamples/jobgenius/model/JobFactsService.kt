package com.canonicalexamples.jobgenius.model

import retrofit2.Call
import retrofit2.http.GET


interface JobFactsService {
    @GET("/positions.json")
    fun getJobs(): Call<JobFacts>

    @GET("/positions.json?location=Remote")
    fun getRemoteJobs(): Call<JobFacts>


}
