package com.example.practicaaaron.ui.viewModel

import android.content.Context
import android.media.audiofx.DynamicsProcessing
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.practicaaaron.clases.basedatos.repositorio.DataUsuarioRepositorioOffline
import com.example.practicaaaron.clases.entidades.DataUsuario
import com.example.practicaaaron.clases.utilidades.Eventos
import com.example.practicaaaron.clases.utilidades.isInternetAvailable
import com.example.practicaaaron.repositorio.RepositorioRetrofit
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.stream.Collectors
import javax.inject.Inject

@HiltViewModel
class UsuariosViewModel @Inject constructor(
    private val dao: DataUsuarioRepositorioOffline
):ViewModel() {

    @RequiresApi(Build.VERSION_CODES.O)
    private val repositorio: RepositorioRetrofit = RepositorioRetrofit()

    private val _usuarios = MutableStateFlow<List<DataUsuario>>(listOf())
    val usuarios: StateFlow<List<DataUsuario>> get() = _usuarios.asStateFlow()

    @RequiresApi(Build.VERSION_CODES.O)
    fun getUsuarios(context: Context){
        viewModelScope.launch (Dispatchers.IO){
            if(isInternetAvailable(context)){
                val todos = repositorio.obtenerTodos()
                _usuarios.value = todos.usuarios.stream().map { DataUsuario(it.idUsuario,it.username,it.idPerfil,it.nombre,it.email) }.collect(Collectors.toList())
                dao.borrarTodos()
                _usuarios.value.stream().forEach { dao.insertar(it) }
            }else{
                _usuarios.value = dao.obtenerTodos()
            }
        }
    }
}