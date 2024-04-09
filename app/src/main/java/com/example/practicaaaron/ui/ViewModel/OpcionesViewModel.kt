package com.example.practicaaaron.ui.ViewModel

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.practicaaaron.R
import com.example.practicaaaron.apiServicio.ApiServicio
import com.example.practicaaaron.clases.pedidos.DataPedido
import com.example.practicaaaron.clases.usuarios.Opcion
import com.example.practicaaaron.repositorio.RepositorioRetrofit
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class OpcionesViewModel(
    private val repositorio: RepositorioRetrofit = RepositorioRetrofit()
):ViewModel() {
    private val _pedidosRepartidor = MutableStateFlow<DataPedido?>(null)
    val pedidosRepartidor: StateFlow<DataPedido?> get() = _pedidosRepartidor.asStateFlow()

    fun obtenerPedidos(){
        viewModelScope.launch {
            Log.i("entro","entro aqui")
            val response = repositorio.recuperarPedidos()
            Log.i("etiqueta",response.toString())
//            _pedidosRepartidor.value = response
        }
    }
}