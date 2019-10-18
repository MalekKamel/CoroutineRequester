package com.coroutinerequester.app.data.restaurants

import com.coroutinerequester.app.data.api.ApiInterface
import com.coroutinerequester.app.data.model.RestaurantResponse

class RestaurantDataSrc(private val api: ApiInterface) {

    suspend fun all(): RestaurantResponse {
        return api.restaurants()
    }

}