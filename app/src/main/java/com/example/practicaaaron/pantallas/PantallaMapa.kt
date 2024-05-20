package com.example.practicaaaron.pantallas

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.practicaaaron.clases.utilidades.LocationService
import com.example.practicaaaron.ui.viewModel.MapaViewModel
import com.example.practicaaaron.ui.viewModel.OpcionesViewModel
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("CoroutineCreationDuringComposition", "StateFlowValueCalledInComposition")
@Composable
fun PantallaMapa(mapaViewModel: MapaViewModel = hiltViewModel()) {

    val conseguirLocalizacion = LocationService()
    val contexto = LocalContext.current
    val latitudUser = remember { mutableFloatStateOf(0f) }
    val altitudUser = remember { mutableFloatStateOf(0f) }
    var posicion by remember { mutableStateOf(LatLng(0.0, 0.0)) }
    var done by remember {mutableStateOf(false)}

    //Intentar que cuando abras google maps salga las ubicaciones
    val ubicaciones = mapaViewModel.ubicaciones.collectAsState().value
    var cameraPositionState = CameraPositionState()

    LaunchedEffect(Unit) {
        val resultado = conseguirLocalizacion.getUserLocation(contexto)
        if (resultado != null) {
            latitudUser.floatValue = resultado.latitude.toFloat()
            altitudUser.floatValue = resultado.longitude.toFloat()
            mapaViewModel.getUbicaciones(contexto)
            posicion = LatLng(latitudUser.floatValue.toDouble(), altitudUser.floatValue.toDouble())
        }
    }

    if(posicion.latitude != 0.0 && posicion.longitude != 0.0){
        cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(LatLng(posicion.latitude, posicion.longitude), 10f)
        }
        done = true
    }

    //Hacer que la camara se centre automaticamente en el usuario
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
            ubicaciones.forEach {
                Marker(
                    state = MarkerState(position = LatLng(it.latitud, it.altitud)),
                    title = it.nombre,
                    icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)
                )
            }
        }
    }
}