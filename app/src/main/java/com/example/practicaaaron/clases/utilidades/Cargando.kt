package com.example.practicaaaron.clases.utilidades

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.practicaaaron.R

@Composable
fun Cargando(){
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(com.example.practicaaaron.ui.theme.cargando) // Fondo semi-transparente
            .zIndex(1f)
            .clickable(enabled = false) {} // Captura los toques y no los deja pasar
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            AnimatedPreloader(modifier = Modifier.size(100.dp), R.raw.animacioncargando, 1.5f)
        }
    }
}

@Composable
fun NoConexion(){
    Box(
        modifier = Modifier
            .fillMaxSize()
            .zIndex(1f)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize().padding(10.dp),
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.Bottom
        ) {
            AnimatedPreloader(modifier = Modifier.size(50.dp), R.raw.nointernet, 1.5f)
        }
    }
}

@Composable
fun Entregando(){
    Box(
        modifier = Modifier
            .fillMaxSize()
            .zIndex(1f)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize().padding(10.dp),
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.Bottom
        ) {
            AnimatedPreloader(modifier = Modifier.size(50.dp), R.raw.animacioncargando, 1.5f)
        }
    }
}