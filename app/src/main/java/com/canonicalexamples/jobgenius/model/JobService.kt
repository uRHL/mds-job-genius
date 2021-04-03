package com.canonicalexamples.jobgenius.model

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface JobService {
    @GET("/positions.json")
    fun getJobs(@Query("page") page: Int, @Query("search") search: String, @Query("location") location: String): Call<List<Job>>

}
