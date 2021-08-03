package com.detroitlabs.weather.ui.current

import android.location.Location
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.detroitlabs.weather.data.WeatherRepository
import com.detroitlabs.weather.data.http.WeatherResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject
import kotlin.math.ceil
import kotlin.math.roundToInt

@HiltViewModel
class CurrentWeatherViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository,
) : ViewModel() {
    private val compositeDisposable = CompositeDisposable()

    private val TAG = "CurrentViewModel"

    val temperature = MutableLiveData("")
    val isLoading = MutableLiveData(false)

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    fun onLocationObtained(location: Location) {
        val disposable = weatherRepository.getCurrentWeather(location.latitude, location.longitude)
            .doOnSubscribe { isLoading.value = true }
            .doFinally { isLoading.value = false }
            .map { toDisplay(it) }
            .subscribe({
                temperature.value = it
            }, {
                Log.e(TAG, "fail to get current weather", it)
            })
        compositeDisposable.add(disposable)
    }

    private fun toDisplay(response: WeatherResponse): String {
        val temperature = "${ceil(response.main.temp).roundToInt()}°"
        return "${response.name}: $temperature"
    }
}