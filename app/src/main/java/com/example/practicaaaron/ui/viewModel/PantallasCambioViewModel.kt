package com.example.practicaaaron.ui.viewModel

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.practicaaaron.BuildConfig
import com.example.practicaaaron.R
import com.example.practicaaaron.clases.basedatos.modulos.ModelMapperInstance
import com.example.practicaaaron.clases.basedatos.repositorio.DataUsuarioRepositorioOffline
import com.example.practicaaaron.clases.basedatos.repositorio.PedidosRepositorioOffline
import com.example.practicaaaron.clases.basedatos.repositorio.TraspasosRepositorioOffline
import com.example.practicaaaron.clases.basedatos.repositorio.UsuarioRepositorioOffline
import com.example.practicaaaron.clases.entidades.DataUsuario
import com.example.practicaaaron.clases.entidades.TransferirPedido
import com.example.practicaaaron.clases.errores.ErrorLog
import com.example.practicaaaron.clases.usuarios.DataUser
import com.example.practicaaaron.clases.utilidades.isInternetAvailable
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

                        val modelmapper = ModelMapperInstance()
                        _usuarios.value.forEach {
                            usuariosDao.add(modelmapper.map(it, DataUsuario::class.java))
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
            if(isInternetAvailable(context)){
                viewModelScope.launch (Dispatchers.IO){
                    val id = uDao.getId()
                    repositorio.mandarError(ErrorLog("conseguirPersonas","App",e.toString(),"",id,BuildConfig.VERSION_CODE,context.toString(),LocalDate.now().toString()))
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun hacerIntercambio(idRecepcion:Int,context: Context){
        try {
            viewModelScope.launch (Dispatchers.IO){
                eventosViewModel.setState(EventosUIState.Cargando)
                val datos = tDao.pasarPedidos()
                val id = uDao.getId()

                val traspaso = TransferirPedido(id,0,idRecepcion)

                if(isInternetAvailable(context)){
                    datos.forEach {
                        traspaso.idPedido = it.idPedido
                        tDao.actualizar(traspaso)

                        repositorio.transferirPedido(traspaso)
                        dDao.transefirPedido(traspaso.idUsuario,traspaso.idPedido,traspaso.idDestinatario)
                    }
                    tDao.borrarHechos()
                    eventosViewModel.setState(EventosUIState.Success(R.string.actualizaciones,R.string.traspasoshechos,LocalDate.now(),-1,-1,true))
                }else{
                    datos.forEach {
                        traspaso.idPedido = it.idPedido
                        traspaso.porEntregar = true
                        tDao.actualizar(traspaso)
                        dDao.transefirPedido(traspaso.idUsuario,traspaso.idPedido,traspaso.idDestinatario)
                    }
                    eventosViewModel.setState(EventosUIState.Success(R.string.actualizaciones,R.string.traspasoshechos,LocalDate.now(),-1,-1,true))
                }
            }
        }catch(e:Exception){
            eventosViewModel.setState(EventosUIState.Error(-1))
            if(isInternetAvailable(context)){
                viewModelScope.launch (Dispatchers.IO){
                    val id = uDao.getId()
                    repositorio.mandarError(ErrorLog("hacerIntercambio","App",e.toString(),"",id,BuildConfig.VERSION_CODE,"$id $context",LocalDate.now().toString()))
                }
            }
        }
    }
}