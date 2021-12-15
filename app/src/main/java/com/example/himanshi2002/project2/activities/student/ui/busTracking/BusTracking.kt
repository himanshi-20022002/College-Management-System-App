package com.example.himanshi2002.project2.activities.student.ui.busTracking

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import com.example.himanshi2002.project2.Constants
import com.example.himanshi2002.project2.R
import com.example.himanshi2002.project2.activities.faculty.DashboardFaculty
import com.google.android.gms.location.*
import com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
import com.google.android.gms.location.LocationRequest.create

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.api.LogDescriptor
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.ktx.Firebase
import android.location.LocationRequest as LocationRequest1
import com.google.android.gms.location.LocationCallback

import com.google.android.gms.location.LocationSettingsResponse

import com.google.android.gms.tasks.OnSuccessListener

import com.google.android.gms.location.LocationServices

import com.google.android.gms.location.SettingsClient

import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.tasks.Task
import com.google.android.gms.location.LocationResult
import android.content.IntentSender
import android.content.IntentSender.SendIntentException
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.core.content.ContentProviderCompat.requireContext

import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.maps.model.Marker

import com.google.android.gms.tasks.OnCompleteListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import com.google.android.gms.maps.GoogleMap as GoogleMap
import android.location.Criteria

import com.google.android.gms.maps.GoogleMap.OnMyLocationChangeListener


class BusTracking : Fragment(), LocationListener {

    private val reference: FirebaseFirestore = FirebaseFirestore.getInstance()
    lateinit var manager: LocationManager

    val MIN_TIME = 3000
    val MIN_DISTANCE = 1

    lateinit var marker : Marker

lateinit var mMap: GoogleMap


    private val callback = OnMapReadyCallback { googleMap ->

mMap=googleMap

        val sydney = LatLng(-34.0, 151.0)
        marker = googleMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))


    }


    @SuppressLint("MissingPermission")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        val view = inflater.inflate(R.layout.fragment_bus_tracking2, container, false)

        manager = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        getLocationUpdate()

        readChanges()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)






    }

    private fun readChanges() {
        GlobalScope.launch {
            val item = reference.collection("Driver").document("Driver-101").get().await()
            val lat=item["latitude"]
            val long=item["longitude"]
            withContext(Dispatchers.Main)
            {


                val location = LatLng(lat as Double, long as Double)
                marker = mMap.addMarker(MarkerOptions().position(location).title("Marker in Sydney"))
                mMap.moveCamera(CameraUpdateFactory.newLatLng(location))
            }
        }
    }


    private fun getLocationUpdate() {


        if (ActivityCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
        ) {
            Log.d("inside", "already has permission ")
            if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                Log.d("inside", "gps ")
                manager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    MIN_TIME.toLong(),
                    MIN_DISTANCE.toFloat(),
                    (this as LocationListener)!!)
                Log.d("manager is1",manager.toString())
            } else if (manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                Log.d("inside", "network")
                manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                    MIN_TIME.toLong(),
                    MIN_DISTANCE.toFloat(),
                    (this as LocationListener)!!)
                Log.d("manager is2",manager.toString())
            }
        } else {
            Log.d("inside", "get permission dialog")
            ActivityCompat.requestPermissions(requireContext() as Activity,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                101)
        }


    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 101) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("inside ", "permission granted")
                getLocationUpdate()
            } else {
                Toast.makeText(requireContext(), "Permission required", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onLocationChanged(location: Location) {
        Log.d("inside ", "onLocationChanged")

        saveLocation(location)
    }

    private fun saveLocation(location: Location) {
        Log.d("location is", location.toString())
        reference.collection("Driver").document("Driver-101")
            .set(location, SetOptions.merge())
            .addOnSuccessListener {
                Log.d(ContentValues.TAG, "DocumentSnapshot successfully written!")


            }
            .addOnFailureListener { e ->
                Log.d("error firestore", e.toString())
            }
    }
}