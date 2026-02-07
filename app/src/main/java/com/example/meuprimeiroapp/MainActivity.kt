package com.example.meuprimeiroapp

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.location.Location
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.meuprimeiroapp.adapter.ItemAdapter
import com.example.meuprimeiroapp.databinding.ActivityMainBinding
import com.example.meuprimeiroapp.model.Item
import com.example.meuprimeiroapp.service.Result
import com.example.meuprimeiroapp.service.RetrofitClient
import com.example.meuprimeiroapp.service.safeApiCall
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * The main activity of the application.
 * This activity displays a list of items and handles location permissions.
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private lateinit var locationPermissionLauncher: ActivityResultLauncher<String>

    /**
     * Called when the activity is first created.
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down then this Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle). Note: Otherwise it is null.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupView()
        requestLocationPermission()
    }

    /**
     * Called when the activity will start interacting with the user.
     */
    override fun onResume() {
        super.onResume()
        fetchItems()
    }

    /**
     * Sets up the views and their listeners.
     */
    private fun setupView() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            fetchItems()
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.addCta.setOnClickListener {
            navigateToNewItem()
        }
        binding.message.setOnClickListener {
            fetchItems()
        }
    }

    private fun navigateToNewItem() {
        startActivity(NewItemActivity.newIntent(this))
    }

    /**
     * Requests location permission from the user.
     */
    private fun requestLocationPermission() {
        // Initializes the FusedLocationPermission
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // Sets up the ActivityResultLauncher for requesting location permission
        locationPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                getLastLocation()
            } else {
                Toast.makeText(this, "Permissão de localização negada", Toast.LENGTH_SHORT).show()
            }
        }

        checkLocationPermissionAndRequest()
    }

    /**
     * Checks for location permission and requests it if necessary.
     */
    private fun checkLocationPermissionAndRequest() {
        when {
            ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) == PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, ACCESS_COARSE_LOCATION) == PERMISSION_GRANTED -> {
                getLastLocation()
            }
            shouldShowRequestPermissionRationale(ACCESS_FINE_LOCATION) -> {
                locationPermissionLauncher.launch(ACCESS_FINE_LOCATION)
            }
            shouldShowRequestPermissionRationale(ACCESS_COARSE_LOCATION) -> {
                locationPermissionLauncher.launch(ACCESS_COARSE_LOCATION)
            }
            else -> {
                locationPermissionLauncher.launch(ACCESS_FINE_LOCATION)
            }
        }
    }

    /**
     * Gets the last known location of the device.
     */
    private fun getLastLocation() {
        if (ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(this, ACCESS_COARSE_LOCATION) != PERMISSION_GRANTED) {
            requestLocationPermission()
            return
        }

        fusedLocationClient.lastLocation.addOnCompleteListener { task: Task<Location> ->
            if (task.isSuccessful && task.result != null) {
                val location = task.result
                val latitude = location.latitude
                val longitude = location.longitude
                Toast.makeText(this, "Latitude: $latitude, Longitude: $longitude", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Não foi possível obter a localização", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * Fetches items from the API.
     */
    private fun fetchItems() {
        CoroutineScope(Dispatchers.IO).launch {
            val result = safeApiCall { RetrofitClient.apiService.getItems() }

            withContext(Dispatchers.Main) {
                binding.swipeRefreshLayout.isRefreshing = false
                when (result) {
                    is Result.Success -> handleOnSuccess(result.data)
                    is Result.Error -> handleOnError()
                }
            }
        }
    }

    /**
     * Handles the successful response from the API.
     * @param items The list of items to be displayed.
     */
    private fun handleOnSuccess(items: List<Item>) {
        if (items.isEmpty()) {
            binding.message.visibility = View.VISIBLE
            binding.message.setText(R.string.no_items)
            binding.recyclerView.visibility = View.GONE
            return
        }
        binding.message.visibility = View.GONE
        binding.recyclerView.visibility = View.VISIBLE
        binding.recyclerView.adapter = ItemAdapter(items) { item ->
            val intent = ItemDetailActivity.newIntent(this, item.id)
            startActivity(intent)
        }
    }

    private fun handleOnError() {
        binding.message.visibility = View.VISIBLE
        binding.message.setText(R.string.generical_error)
        binding.recyclerView.visibility = View.GONE
    }
}