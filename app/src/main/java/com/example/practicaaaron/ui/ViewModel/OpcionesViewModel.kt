package com.example.practicaaaron.ui.ViewModel

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.practicaaaron.clases.pedidos.DataPedido
import com.example.practicaaaron.clases.usuarios.Data
import com.example.practicaaaron.clases.usuarios.UsuarioLogin
import com.example.practicaaaron.repositorio.RepositorioRetrofit
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
class OpcionesViewModel(
    private val repositorio: RepositorioRetrofit = RepositorioRetrofit()
):ViewModel() {
    private val _pedidosRepartidor = MutableStateFlow<DataPedido?>(null)
    private val _informacionUsuario = MutableStateFlow<Data?>(null)
    val pedidosRepartidor: StateFlow<DataPedido?> get() = _pedidosRepartidor.asStateFlow()
    val informacionUsuario: StateFlow<Data?> get() = _informacionUsuario.asStateFlow()

    fun obtenerPedidos(){
        viewModelScope.launch {
            Log.i("entro","entro aqui")
            val response = repositorio.recuperarPedidos()
            Log.i("etiqueta",response.toString())
            _pedidosRepartidor.value = response
        }
    }

    fun hacerLogin(usuarioLogin: UsuarioLogin) {
        viewModelScope.launch {
            Log.i("entro","entro aqui")
            val response = repositorio.hacerLogin(usuarioLogin)
            _informacionUsuario.value = response
            Log.i("etiqueta","$response")
        }
    }

}