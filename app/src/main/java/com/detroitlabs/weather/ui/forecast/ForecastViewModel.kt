package com.detroitlabs.weather.ui.forecast

import android.location.Location
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.detroitlabs.weather.data.WeatherRepository
import com.detroitlabs.weather.data.http.ForecastResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.disposables.CompositeDisposable
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.time.format.TextStyle
import java.util.Locale
import javax.inject.Inject
import kotlin.math.ceil
import kotlin.math.roundToInt

@HiltViewModel
class ForecastViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository
) : ViewModel() {

    private val TAG = "ForecastViewModel"
    private val compositeDisposable = CompositeDisposable()

    val forecasts: LiveData<List<ForecastItemDisplay>>
        get() = _forecasts
    private val _forecasts = MutableLiveData<List<ForecastItemDisplay>>()

    val isLoading = MutableLiveData(false)

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    fun onLocationObtained(location: Location) {
        val disposable = weatherRepository.getFiveDayForecast(location.latitude, location.longitude)
            .doOnSubscribe { isLoading.value = true }
            .doFinally { isLoading.value = false }
            .map { toDisplay(it) }
            .subscribe({
                _forecasts.value = it
            }, {
                Log.e(TAG, "fail to get forecast", it)
            })

        compositeDisposable.add(disposable)
    }

    private fun toDisplay(response: ForecastResponse): List<ForecastItemDisplay> {
        fun Double.toDisplay(): String = "${ceil(this).roundToInt()}°"

        fun Long.toDayOffWeek(): String {
            val dateTime = Instant.ofEpochSecond(this).atZone(ZoneId.systemDefault())
            return dateTime.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault())
        }

        fun Long.toTime(): String {
            val dateTime = Instant.ofEpochSecond(this).atZone(ZoneId.systemDefault())
            val formatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)
            return dateTime.format(formatter)
        }
        return response.list.map {
            ForecastItemDisplay(
                temperature = it.main.temp.toDisplay(),
                dayOfWeek = it.dt.toDayOffWeek(),
                time = it.dt.toTime()
            )
        }
    }
}