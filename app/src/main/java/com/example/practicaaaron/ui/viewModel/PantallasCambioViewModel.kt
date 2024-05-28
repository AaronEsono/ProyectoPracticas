package com.example.practicaaaron.ui.viewModel

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.practicaaaron.R
import com.example.practicaaaron.clases.basedatos.modulos.ModelMapperInstance
import com.example.practicaaaron.clases.basedatos.repositorio.DataUsuarioRepositorioOffline
import com.example.practicaaaron.clases.basedatos.repositorio.PedidosRepositorioOffline
import com.example.practicaaaron.clases.basedatos.repositorio.TraspasosRepositorioOffline
import com.example.practicaaaron.clases.basedatos.repositorio.UsuarioRepositorioOffline
import com.example.practicaaaron.clases.entidades.DataUsuario
import com.example.practicaaaron.clases.entidades.TransferirPedido
import com.example.practicaaaron.clases.usuarios.DataUser
import com.example.practicaaaron.clases.utilidades.isInternetAvailable
import com.example.practicaaaron.clases.utilidades.listadoTraspasos
import com.example.practicaaaron.repositorio.RepositorioRetrofit
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.stream.Collectors
import javax.inject.Inject

@HiltViewModel
class PantallasCambioViewModel @Inject constructor(
    private val pDao: DataUsuarioRepositorioOffline,
    private val uDao: UsuarioRepositorioOffline,
    private val dDao: PedidosRepositorioOffline,
    private val tDao: TraspasosRepositorioOffline,
    private val eventosViewModel: EventosViewModel = EventosViewModel()
): ViewModel(){

    @RequiresApi(Build.VERSION_CODES.O)
    private val repositorio: RepositorioRetrofit = RepositorioRetrofit()

    private val _usuarios = MutableStateFlow<MutableList<DataUser>>(mutableListOf())
    val usuarios:StateFlow<MutableList<DataUser>> get() = _usuarios.asStateFlow()

    @RequiresApi(Build.VERSION_CODES.O)
    fun conseguirPersonas(context: Context){
        try{
            viewModelScope.launch (Dispatchers.IO){
                eventosViewModel.setState(EventosUIState.Cargando)
                val id = uDao.getId()
                if(isInternetAvailable(context)){
                    _usuarios.value = repositorio.obtenerTodos().usuarios.toMutableList()

                    if(_usuarios.value.isNotEmpty()){
                        _usuarios.value = _usuarios.value.stream().filter { it.tipoPerfil == 1 && it.idUsuario != id }.collect(Collectors.toList())
                        val usuariosDao:MutableList<DataUsuario> = mutableListOf()

                        _usuarios.value.forEach {
                            usuariosDao.add(ModelMapperInstance.mapper.map(it, DataUsuario::class.java))
                        }

                        usuariosDao.forEach { pDao.insertar(it) }

                        eventosViewModel.setState(EventosUIState.Done)
                    }else{
                        eventosViewModel.setState(EventosUIState.Error(R.string.falloEntrega))
                    }
                }else{
                    val datos = pDao.obtenerTodos().toMutableList()
                    val usuariosDao:MutableList<DataUser> = mutableListOf()

                    datos.forEach {
                        usuariosDao.add(DataUser(it.idUsuario,it.username,it.idPerfil,1,it.nombre,it.email,"",0,""))
                    }

                    _usuarios.value = usuariosDao
                    eventosViewModel.setState(EventosUIState.Error(R.string.falloEntrega))
                }
            }
        }catch (e:Exception){
            eventosViewModel.setState(EventosUIState.Error(-1))
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun hacerIntercambio(idRecepcion:Int,context: Context){
        try {
            viewModelScope.launch (Dispatchers.IO){
                eventosViewModel.setState(EventosUIState.Cargando)
                val datos = listadoTraspasos
                val id = uDao.getId()

                val traspaso = TransferirPedido(id,0,idRecepcion)

                if(isInternetAvailable(context)){
                    datos.forEach {
                        traspaso.idPedido = it
                        repositorio.transferirPedido(traspaso)
                        dDao.transefirPedido(traspaso.idUsuario,traspaso.idPedido,traspaso.idDestinatario)
                    }
                    eventosViewModel.setState(EventosUIState.Success(R.string.actualizaciones,R.string.traspasoshechos,LocalDate.now(),-1,-1,true))
                }else{
                    datos.forEach {
                        traspaso.idPedido = it
                        tDao.insertar(traspaso)
                        dDao.transefirPedido(traspaso.idUsuario,traspaso.idPedido,traspaso.idDestinatario)
                    }
                    eventosViewModel.setState(EventosUIState.Error(R.string.noConexion))
                }
                listadoTraspasos = mutableListOf()
            }
        }catch(e:Exception){
            eventosViewModel.setState(EventosUIState.Error(-1))
        }
    }


}