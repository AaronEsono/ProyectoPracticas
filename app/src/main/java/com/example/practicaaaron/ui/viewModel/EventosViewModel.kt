package com.example.practicaaaron.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class EventosViewModel  @Inject constructor(): ViewModel() {

    private var _uiState = MutableStateFlow<EventosUIState>(EventosUIState.Done)
    val uiState:StateFlow<EventosUIState> get() = _uiState.asStateFlow()

    fun setState(eventosUIState: EventosUIState) = viewModelScope.launch{
        _uiState.value = eventosUIState
    }
}

sealed class EventosUIState{
    data class Error(val texto:Int): EventosUIState()
    data object Cargando: EventosUIState()
    data object Done: EventosUIState()
    data class Success(val texto:Int,val textoTitulo:Int, val fecha: LocalDate, val id:Int, val todos:Int, val entrega:Boolean) : EventosUIState()
}