package com.coroutinerequester.app.data.api

import com.coroutinerequester.app.data.model.RestaurantResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {

    @GET("restaurants")
    suspend fun restaurants(@Query("country") country: String = "US"): RestaurantResponse
}