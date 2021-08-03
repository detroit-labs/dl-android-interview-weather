package com.detroitlabs.weather.data.http

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class WeatherResponse(
    val main: Main,
    val name: String
) {
    @JsonClass(generateAdapter = true)
    class Main(
        val temp: Double
    )
}