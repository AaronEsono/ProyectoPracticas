package com.example.practicaaaron.ui.viewModel

import androidx.annotation.OptIn
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EventosViewModel  @Inject constructor(): ViewModel() {

    private var _uiState = MutableStateFlow<EventosUIState>(EventosUIState.Done)
    val uiState:StateFlow<EventosUIState> get() = _uiState.asStateFlow()

    fun setState(eventosUIState: EventosUIState) = viewModelScope.launch{
        _uiState.value = eventosUIState
    }
}

sealed class EventosUIState(){
    data class Error(val texto:String): EventosUIState()
    data object Cargando: EventosUIState()
    data object Done: EventosUIState()
    data class Success(val texto:String) : EventosUIState()
}