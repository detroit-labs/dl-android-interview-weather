package com.detroitlabs.weather.data.http

import com.detroitlabs.weather.BuildConfig
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET("weather")
    fun getCurrentWeather(
        zip: String,
        @Query("zip") zipParam: String = "$zip,us"
    ): Single<WeatherResponse>

    @GET("weather")
    fun getCurrentWeather(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("units") units: String = "imperial",
        @Query("appid") appId: String = BuildConfig.APP_ID
    ): Single<WeatherResponse>

    @GET("forecast")
    fun getFiveDayForecast(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("units") units: String = "imperial",
        @Query("appid") appId: String = BuildConfig.APP_ID
    ): Single<ForecastResponse>
}