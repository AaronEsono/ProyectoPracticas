package com.example.practicaaaron


import android.content.ContentValues.TAG
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.practicaaaron.navegador.AppNavHost
import com.example.practicaaaron.ui.ViewModel.OpcionesViewModel
import com.example.practicaaaron.ui.theme.PracticaAaronTheme
import com.example.practicaaaron.ui.theme.colorPrimario
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseApp
import com.google.firebase.installations.FirebaseInstallations

//import com.google.mlkit.vision.barcode.common.Barcode
//import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
//import com.google.mlkit.vision.codescanner.GmsBarcodeScanning


/**
 * @author Aarón Esono Borreguero
 * App para controlar los repartos de los trabajadores
 */

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)

        setContent {
            //Creación de un navController para navegar entre las distinas pantallas
            val navController = rememberNavController()

            //Creación de un viewModel para las pantallas
            val opcionesViewModel by viewModels<OpcionesViewModel>()

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