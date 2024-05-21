package com.example.practicaaaron


import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.practicaaaron.clases.utilidades.cargando
import com.example.practicaaaron.clases.utilidades.mensajeError
import com.example.practicaaaron.navegador.AppNavHost
import com.example.practicaaaron.ui.theme.PracticaAaronTheme
import com.example.practicaaaron.ui.theme.colorPrimario
import com.example.practicaaaron.ui.viewModel.EventosUIState
import com.example.practicaaaron.ui.viewModel.EventosViewModel
import com.google.firebase.FirebaseApp
import dagger.hilt.android.AndroidEntryPoint

/**
 * @author Aarón Esono Borreguero
 * App para controlar los repartos de los trabajadores
 */

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val eventosViewModel:EventosViewModel by viewModels()

    @SuppressLint("StateFlowValueCalledInComposition")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        setContent {
            //Creación de un navController para navegar entre las distintas pantallas
            val navController = rememberNavController()
            val uiState = eventosViewModel.uiState.collectAsState().value
            PracticaAaronTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = colorPrimario
                ) {
                    //Función para navegar entre las distintas pantallas
                    AppNavHost(navController = navController)

                    when(uiState){
                        EventosUIState.Cargando -> {cargando()}
                        is EventosUIState.Error -> {mensajeError(texto = uiState.texto){eventosViewModel.setState(EventosUIState.Done)}}
                        EventosUIState.Done -> {}
                        is EventosUIState.Success -> {mensajeError(texto = uiState.texto) {eventosViewModel.setState(EventosUIState.Done)}}
                    }
                }
            }
        }
    }
}