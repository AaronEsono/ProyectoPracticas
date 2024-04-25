package com.example.practicaaaron.pantallas

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccessAlarm
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.practicaaaron.clases.ubicaciones.Ubicacion
import com.example.practicaaaron.clases.utilidades.LocationService
import com.example.practicaaaron.ui.ViewModel.OpcionesViewModel
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("CoroutineCreationDuringComposition", "StateFlowValueCalledInComposition")
@Composable
fun pantallaMapa(navController: NavHostController, opcionesViewModel: OpcionesViewModel) {
    val conseguirLocalizacion = LocationService()
    val contexto = LocalContext.current
    var latitudUser = remember { mutableStateOf(0f) }
    var altitudUser = remember { mutableStateOf(0f) }
    var posicion by remember { mutableStateOf(LatLng(0.0, 0.0)) }
    var done by remember {mutableStateOf(false)}

    //Intentar que cuando abras google maps salga las ubicaciones
    var ubicaciones = opcionesViewModel?.ubicaciones?.collectAsState()?.value

    LaunchedEffect(Unit) {
        val resultado = conseguirLocalizacion.getUserLocation(contexto)
        if (resultado != null) {
            latitudUser.value = resultado.latitude.toFloat()
            altitudUser.value = resultado.longitude.toFloat()
            opcionesViewModel.obtenerPedidos()
            posicion = LatLng(latitudUser.value.toDouble(), altitudUser.value.toDouble())
        }
        done = true
    }


    //Hacer que la camara se centre automaticamente en el usuario
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(40.4495851, -3.6920861), 10f)
    }

    if(done){
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState
        ) {
            Marker(
                state = MarkerState(position = posicion),
                title = "Estas aquí",
                snippet = "Estas aquí",
                icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)
            )
            ubicaciones?.forEach {
                Marker(
                    state = MarkerState(position = LatLng(it.latitud, it.altitud)),
                    title = "${it.nombre}",
                    icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)
                )
            }
        }
    }
}