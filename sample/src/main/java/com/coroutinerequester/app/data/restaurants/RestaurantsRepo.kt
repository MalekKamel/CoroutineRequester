package com.coroutinerequester.app.data.restaurants

import com.coroutinerequester.app.data.model.RestaurantResponse

class RestaurantsRepo(private val restaurantDataSrc: RestaurantDataSrc) {

    suspend fun all(): RestaurantResponse {
        return restaurantDataSrc.all()
    }


}
