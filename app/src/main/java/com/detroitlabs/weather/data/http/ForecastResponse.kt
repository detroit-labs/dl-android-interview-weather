package com.detroitlabs.weather.data.http

import com.squareup.moshi.JsonClass

/**
 * 5 day weather forecast response
 * https://openweathermap.org/forecast5#parameter
 */
@JsonClass(generateAdapter = true)
class ForecastResponse(
    val list: List<StepResponse>,
    val city: City
) {
    @JsonClass(generateAdapter = true)
    class StepResponse(
        val dt: Long,
        val main: Main
    ) {
        @JsonClass(generateAdapter = true)
        class Main(
            val temp: Double
        )
    }

    @JsonClass(generateAdapter = true)
    class City(
        val name: String
    )
}