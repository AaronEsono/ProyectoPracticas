package com.example.practicaaaron.ui.viewModel

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.practicaaaron.clases.basedatos.repositorio.PedidosRepositorioOffline
import com.example.practicaaaron.clases.basedatos.repositorio.UsuarioRepositorioOffline
import com.example.practicaaaron.clases.ubicaciones.Ubicacion
import com.example.practicaaaron.clases.utilidades.isInternetAvailable
import com.example.practicaaaron.repositorio.RepositorioRetrofit
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class MapaViewModel @Inject constructor(
    private val dao: PedidosRepositorioOffline,
    private val uDao: UsuarioRepositorioOffline,
    private val eventosViewModel: EventosViewModel = EventosViewModel()
): ViewModel(){

    @RequiresApi(Build.VERSION_CODES.O)
    private val repositorio: RepositorioRetrofit = RepositorioRetrofit()

    private val _ubicaciones = MutableStateFlow<MutableList<Ubicacion>>(mutableListOf())
    val ubicaciones:StateFlow<MutableList<Ubicacion>> get() = _ubicaciones.asStateFlow()

    @RequiresApi(Build.VERSION_CODES.O)
    fun getUbicaciones(context:Context){
        viewModelScope.launch (Dispatchers.IO){
            eventosViewModel.setState(EventosUIState.Cargando)
            val id = uDao.getId()
            if(isInternetAvailable(context)){
                val pedidos2 = repositorio.recuperarPedidos(id, LocalDate.now())
                pedidos2?.data?.pedidos?.stream()?.forEach { ubicaciones.value.add(Ubicacion(it.latitud.toDouble(),it.altitud.toDouble(),it.nombre)) }
                eventosViewModel.setState(EventosUIState.Done)
            }else{
                val pedidos = dao.getPedidosHoy(id,LocalDate.now().toString())
                pedidos.stream().forEach { ubicaciones.value.add(Ubicacion(it.pedido.latitud.toDouble(),it.pedido.altitud.toDouble(),it.pedido.nombre)) }
                eventosViewModel.setState(EventosUIState.Error("No hay conexión. Mostrando el mapa."))
            }
        }
    }
}