package com.example.practicaaaron.pantallas

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.practicaaaron.clases.utilidades.AnimatedPreloader
import com.example.practicaaaron.ui.ViewModel.OpcionesViewModel

@Composable
fun hecho(navHostController: NavHostController, opcionesViewModel: OpcionesViewModel) {
    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        AnimatedPreloader(modifier = Modifier.size(250.dp))
        Spacer(modifier = Modifier.padding(0.dp,10.dp))
        Text(text = "Enhorabuena. Has hecho todas las entregas")
        Spacer(modifier = Modifier.padding(0.dp,10.dp))
        Button(onClick = { navHostController.navigate("pedidos") }, modifier = Modifier.height(100.dp)) {
            Text(text = "Volver a pedidos")
        }
    }
}