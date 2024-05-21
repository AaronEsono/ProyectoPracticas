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
    private val dao: UsuarioRepositorioOffline,
    private val eventosViewModel: EventosViewModel = EventosViewModel()
): ViewModel(){

    private val _tipoPerfil = MutableStateFlow(-1)
    val tipoPerfil:StateFlow<Int> get() = _tipoPerfil.asStateFlow()

    private val _idUser = MutableStateFlow(-1)
    val idUser:StateFlow<Int> get() = _idUser.asStateFlow()

    fun getDatos(){
        viewModelScope.launch (Dispatchers.IO){
            eventosViewModel.setState(EventosUIState.Cargando)
            _tipoPerfil.value = dao.getTipoPerfil()
            _idUser.value = dao.getId()
            eventosViewModel.setState(EventosUIState.Done)
        }
    }
}