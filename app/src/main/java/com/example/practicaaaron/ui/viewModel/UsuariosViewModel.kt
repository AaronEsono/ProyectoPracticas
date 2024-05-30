package com.example.practicaaaron.ui.viewModel

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.practicaaaron.BuildConfig
import com.example.practicaaaron.R
import com.example.practicaaaron.clases.basedatos.repositorio.DataUsuarioRepositorioOffline
import com.example.practicaaaron.clases.basedatos.repositorio.UsuarioRepositorioOffline
import com.example.practicaaaron.clases.entidades.DataUsuario
import com.example.practicaaaron.clases.errores.ErrorLog
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
class UsuariosViewModel @Inject constructor(
    private val dao: DataUsuarioRepositorioOffline,
    private val uDao: UsuarioRepositorioOffline,
    private val eventosViewModel: EventosViewModel = EventosViewModel()
):ViewModel() {

    @RequiresApi(Build.VERSION_CODES.O)
    private val repositorio: RepositorioRetrofit = RepositorioRetrofit()

    private val _usuarios = MutableStateFlow<List<DataUsuario>>(listOf())
    val usuarios: StateFlow<List<DataUsuario>> get() = _usuarios.asStateFlow()

    @RequiresApi(Build.VERSION_CODES.O)
    fun getUsuarios(context: Context){
        try {
            viewModelScope.launch (Dispatchers.IO){
                eventosViewModel.setState(EventosUIState.Cargando)
                if(isInternetAvailable(context)){
                    val todos = repositorio.obtenerTodos()
                    _usuarios.value = todos.usuarios.stream().map { DataUsuario(it.idUsuario,it.username,it.idPerfil,it.nombre,it.email) }.collect(Collectors.toList())
                    dao.borrarTodos()
                    _usuarios.value.stream().forEach { dao.insertar(it) }
                    eventosViewModel.setState(EventosUIState.Done)
                }else{
                    _usuarios.value = dao.obtenerTodos()
                    if(_usuarios.value.isEmpty()){
                        eventosViewModel.setState(EventosUIState.Error(R.string.noConexion))
                    }else{
                        eventosViewModel.setState(EventosUIState.Error(R.string.recuperarConexion))
                    }
                }
            }
        }catch (e:Exception){
            if(isInternetAvailable(context)){
                val id = uDao.getId()
                val err = ErrorLog("getUsuarios", "App", "$e", "", id, BuildConfig.VERSION_CODE,"", LocalDate.now().toString())
                viewModelScope.launch {
                    repositorio.mandarError(err)
                }
            }
            eventosViewModel.setState(EventosUIState.Error(R.string.algo))
        }
    }
}