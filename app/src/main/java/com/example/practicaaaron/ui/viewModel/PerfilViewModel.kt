package com.example.practicaaaron.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.practicaaaron.clases.basedatos.repositorio.UsuarioRepositorioOffline
import com.example.practicaaaron.clases.entidades.Usuario
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PerfilViewModel @Inject constructor(
    private val dao: UsuarioRepositorioOffline
) : ViewModel(){

    private val _usuario = MutableStateFlow(Usuario())
    val usuario: StateFlow<Usuario> get() = _usuario.asStateFlow()

    fun cogerDatos(){
        viewModelScope.launch (Dispatchers.IO){
            _usuario.value = dao.getUsuario()
        }
    }
}