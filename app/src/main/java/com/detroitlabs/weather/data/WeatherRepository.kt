package com.detroitlabs.weather.data

import com.detroitlabs.weather.data.http.ForecastResponse
import com.detroitlabs.weather.data.http.WeatherApi
import com.detroitlabs.weather.data.http.WeatherResponse
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class WeatherRepository @Inject constructor(
    private val weatherApi: WeatherApi
) {
    fun getCurrentWeather(lat: Double, long: Double): Single<WeatherResponse> {
        return weatherApi.getCurrentWeather(lat, long)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun getFiveDayForecast(lat: Double, long: Double): Single<ForecastResponse> {
        return weatherApi.getFiveDayForecast(lat, long)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}