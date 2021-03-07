package com.canonicalexamples.mypantry.model

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface FoodFactsService {
    @GET("/positions.json")
    fun getOffers(): Call<FoodFacts>

    @GET("/positions.json?location=Remote")
    fun getRemoteOffers(): Call<FoodFacts>


}
