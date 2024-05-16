package com.example.practicaaaron.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.practicaaaron.clases.basedatos.repositorio.UsuarioRepositorioOffline
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MenuViewModel @Inject constructor(
    private val dao: UsuarioRepositorioOffline
): ViewModel(){

    private val _tipoPerfil = MutableStateFlow(-1)
    val tipoPerfil:StateFlow<Int> get() = _tipoPerfil.asStateFlow()

    private val _idUser = MutableStateFlow(-1)
    val idUser:StateFlow<Int> get() = _idUser.asStateFlow()

    fun getTipoPerfil(){
        viewModelScope.launch (Dispatchers.IO){
            _tipoPerfil.value = dao.getTipoPerfil()
        }
    }

    fun setId(){
        viewModelScope.launch (Dispatchers.IO){
            _idUser.value = dao.getId()
        }
    }
}