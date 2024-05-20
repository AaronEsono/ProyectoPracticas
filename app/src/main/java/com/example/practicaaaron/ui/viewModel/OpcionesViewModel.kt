package com.example.practicaaaron.ui.viewModel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.practicaaaron.clases.basedatos.repositorio.UsuarioRepositorioOffline
import com.example.practicaaaron.clases.incidencias.Entregado
import com.example.practicaaaron.clases.pedidos.DataPedido
import com.example.practicaaaron.clases.pedidos.Informacion
import com.example.practicaaaron.clases.pedidos.PedidoCab
import com.example.practicaaaron.clases.usuarios.Data
import com.example.practicaaaron.clases.usuarios.Usuarios
import com.example.practicaaaron.clases.utilidades.coloresIncidencias
import com.example.practicaaaron.repositorio.RepositorioRetrofit
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class OpcionesViewModel @Inject constructor(
) : ViewModel() {

    private val repositorio: RepositorioRetrofit = RepositorioRetrofit()
    //Variables utilizadas en: Login
    // Valores: -1 si no hay usuario, 1 si es usuario, 2 si es admin
    private val _isLogged = MutableStateFlow<Int?>(-1)
    val isLogged: StateFlow<Int?> get() = _isLogged.asStateFlow()

    private val _informacionUsuario = MutableStateFlow<Data?>(null)

    fun mandarCerrarSesion(){
        viewModelScope.launch {
            _informacionUsuario.value?.dataUser?.idUsuario?.let { repositorio.cerrarSesion(it) }
        }
    }
}