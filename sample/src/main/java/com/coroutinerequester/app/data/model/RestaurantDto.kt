package com.coroutinerequester.app.data.model

import com.coroutinerequester.app.data.mapper.Mapper
import com.google.gson.annotations.SerializedName

data class RestaurantDto(
        var name: String,
        var city: String,

        @SerializedName(value = "image_url")
        var poster: String

)

class RestaurantMapper: Mapper<RestaurantDto, Restaurant> {
        override fun map(input: RestaurantDto): Restaurant {
                return Restaurant(
                        name = input.name,
                        city = input.city,
                        poster = input.poster
                )
        }
}
