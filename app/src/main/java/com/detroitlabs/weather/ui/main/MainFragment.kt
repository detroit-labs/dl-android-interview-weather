package com.detroitlabs.weather.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.detroitlabs.weather.R
import com.detroitlabs.weather.ui.current.CurrentWeatherFragment
import com.detroitlabs.weather.ui.forecast.ForecastFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView

class MainFragment : Fragment(), NavigationBarView.OnItemSelectedListener,
    NavigationBarView.OnItemReselectedListener {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<BottomNavigationView>(R.id.bottom_navigation).let { bottomView ->
            bottomView.setOnItemSelectedListener(this)
            bottomView.setOnItemReselectedListener(this)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.current_weather -> showCurrentWeather()
            R.id.five_day_forecast -> showFiveDayForecast()
        }
        return true
    }

    private fun showFiveDayForecast() {
        childFragmentManager.commit {
            replace(R.id.main_content, ForecastFragment())
        }
    }

    private fun showCurrentWeather() {
        childFragmentManager.commit {
            replace(R.id.main_content, CurrentWeatherFragment())
        }
    }

    override fun onNavigationItemReselected(item: MenuItem) {
        /* do nothing */
    }
}