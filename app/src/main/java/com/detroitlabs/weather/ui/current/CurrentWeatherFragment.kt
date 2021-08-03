package com.detroitlabs.weather.ui.current

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.detroitlabs.weather.databinding.FragmentCurrentWeatherBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationTokenSource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CurrentWeatherFragment : Fragment() {

    private val TAG = "CurrentFragment"
    private lateinit var binding: FragmentCurrentWeatherBinding
    private lateinit var activityResultLauncher: ActivityResultLauncher<String>

    private val viewModel: CurrentWeatherViewModel by viewModels()
    private val cancellationTokenSource = CancellationTokenSource()
    private val fusedLocationClient: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(requireActivity())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityResultLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
                if (granted) {
                    binding.getWeatherButton.isVisible = false
                    getLocation()
                } else {
                    binding.getWeatherButton.isVisible = false
                    binding.permissionDeniedMessage.isVisible = true
                    Log.i(TAG, "Location permission was not granted")
                }
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCurrentWeatherBinding.inflate(inflater, container, false)
        binding.vm = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupButton()
        if (hasLocationPermission()) {
            binding.getWeatherButton.isVisible = false
            getLocation()
        } else {
            binding.getWeatherButton.isVisible = true
        }
    }

    private fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    @SuppressLint("MissingPermission")
    private fun getLocation() {
        val currentLocationTask = fusedLocationClient.getCurrentLocation(
            LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY,
            cancellationTokenSource.token
        )

        currentLocationTask.addOnCompleteListener { task ->
            val result = if (task.isSuccessful && task.result != null) {
                val result: Location = task.result
                viewModel.onLocationObtained(result)
                "Location (success): ${result.latitude}, ${result.longitude}"
            } else {
                val exception = task.exception
                "Location (failure): $exception"
            }
            Log.d(TAG, "getCurrentLocation() result: $result")
        }
    }

    private fun setupButton() {
        binding.getWeatherButton.setOnClickListener {
            activityResultLauncher.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
        }
    }
}