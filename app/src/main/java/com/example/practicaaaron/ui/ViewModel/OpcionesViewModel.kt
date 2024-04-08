package com.example.practicaaaron.ui.ViewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.util.copy
import com.example.practicaaaron.R
import com.example.practicaaaron.objetos.Opcion
import com.example.practicaaaron.objetos.OpcionState
import kotlinx.coroutines.launch

class OpcionesViewModel:ViewModel() {
    var opciones by mutableStateOf(listOf<Opcion>())
        private set

    init {
        viewModelScope.launch {
            opciones = listOf(
                Opcion(R.drawable.imagen2,"Pedidos","pedidos"),
                Opcion(R.drawable.imagen3,"Rutas","ruta"),
                Opcion(R.drawable.imagen4,"Logistica","logistica")
            )
        }
    }
}

//Opcion(R.drawable.imagen2,"Pedidos","pedidos"),
//Opcion(R.drawable.imagen3,"Rutas","ruta"),
//Opcion(R.drawable.imagen4,"Logistica","logistica")