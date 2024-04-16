package com.example.practicaaaron.clases.utilidades

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationManager
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine

class LocationService{
    @androidx.annotation.OptIn(UnstableApi::class)
    @OptIn(ExperimentalCoroutinesApi::class)
    @SuppressLint("MissingPermission")
    suspend fun getUserLocation(context: Context): Location?{

        Log.i("contexto","$context")

        val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
        val isUserLocationPermissionGranted = true
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val isGPSEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)

        if(!isGPSEnabled || !isUserLocationPermissionGranted){
            return null
        }

        return suspendCancellableCoroutine { cont ->
            fusedLocationProviderClient.lastLocation.apply {
                if(isComplete){
                    if(isSuccessful){
                        cont.resume(result){}
                    }else{
                        cont.resume(null){}
                    }
                    return@suspendCancellableCoroutine
                }
                addOnSuccessListener {
                    cont.resume(it){}
                }
                addOnFailureListener{
                    cont.resume(null){}
                }
                addOnCanceledListener {
                    cont.resume(null){}
                }
            }
        }
    }
}