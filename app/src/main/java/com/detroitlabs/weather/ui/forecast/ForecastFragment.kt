package com.detroitlabs.weather.ui.forecast

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.detroitlabs.weather.R
import com.detroitlabs.weather.databinding.FragmentForecastBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ForecastFragment : Fragment() {
    private val TAG = "ForecastFragment"
    private lateinit var binding: FragmentForecastBinding
    private val viewModel: ForecastViewModel by viewModels()

    private val fusedLocationClient: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentForecastBinding.inflate(inflater, container, false).apply {
            binding = this
            vm = viewModel
            lifecycleOwner = viewLifecycleOwner
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = ForecastRvAdapter()
        view.findViewById<RecyclerView>(R.id.five_day_forecast_list).run {
            this.adapter = adapter
        }
        viewModel.forecasts.observe(viewLifecycleOwner) {
            adapter.setItems(it)
        }

        getLastLocation()
    }

    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        fusedLocationClient.lastLocation
            .addOnCompleteListener { taskLocation ->
                if (taskLocation.isSuccessful && taskLocation.result != null) {
                    val location = taskLocation.result
                    viewModel.onLocationObtained(location)
                } else {
                    Log.w(TAG, "getLastLocation:exception", taskLocation.exception)
                }
            }
    }
}