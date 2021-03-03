package com.canonicalexamples.mypantry.model

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path


interface FoodFactsService {
    @GET("/todos/{id}")
    fun getTodo(@Path(value = "id") id: Int): Call<FoodFacts>
}
