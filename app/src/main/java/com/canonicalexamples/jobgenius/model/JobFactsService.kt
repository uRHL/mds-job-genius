package com.canonicalexamples.jobgenius.model

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface JobFactsService {
    @GET("/positions.json")
    fun getJobs(@Query("page") page: String): Call<JobFacts>

    @GET("/positions.json?location=Remote")
    fun getRemoteJobs(@Query("page") page: String): Call<JobFacts>

    @GET("/positions.json")
    fun getJobsWithDescription(@Query("page") page: String, @Query("description") description: String): Call<JobFacts>

    @GET("/positions.json?location=Remote")
    fun getRemoteJobsWithDescription(@Query("page") page: String, @Query("description") description: String): Call<JobFacts>


}
