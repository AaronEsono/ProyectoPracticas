package com.example.practicaaaron


import android.os.Build


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.practicaaaron.ui.theme.PracticaAaronTheme
import androidx.navigation.compose.rememberNavController
import com.example.practicaaaron.navegador.AppNavHost
import com.example.practicaaaron.ui.ViewModel.OpcionesViewModel

/**
 * @author Aarón Esono Borreguero
 * App para controlar los repartos de los trabajadores
 */

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            //Creación de un navController para navegar entre las distinas pantallas
            val navController = rememberNavController()

            //Creación de un viewModel para las pantallas
            val opcionesViewModel by viewModels<OpcionesViewModel>()

            PracticaAaronTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    //Función para navegar entre las distintas pantallas
                    AppNavHost(navController = navController, opcionesViewModel)
                }
            }
        }
    }
}