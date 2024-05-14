package com.example.practicaaaron


import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.practicaaaron.navegador.AppNavHost
import com.example.practicaaaron.ui.theme.PracticaAaronTheme
import com.example.practicaaaron.ui.theme.colorPrimario
import com.example.practicaaaron.ui.viewModel.OpcionesViewModel
import com.google.firebase.FirebaseApp
import dagger.hilt.android.AndroidEntryPoint

/**
 * @author Aarón Esono Borreguero
 * App para controlar los repartos de los trabajadores
 */

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    val opcionesViewModel :OpcionesViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        setContent {
            //Creación de un navController para navegar entre las distintas pantallas
            val navController = rememberNavController()

            PracticaAaronTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = colorPrimario
                ) {
                    //Función para navegar entre las distintas pantallas
                    AppNavHost(navController = navController, opcionesViewModel)
                }
            }
        }
    }
}