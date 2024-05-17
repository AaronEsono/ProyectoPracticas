package com.example.practicaaaron.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.practicaaaron.clases.basedatos.repositorio.PedidosRepositorioOffline
import com.example.practicaaaron.clases.entidades.pedidos.PedidoEntero
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InfoProductoViewModel @Inject constructor(
    private val dao: PedidosRepositorioOffline
) : ViewModel(){

    private val _pedido = MutableStateFlow(PedidoEntero())
    val pedido:StateFlow<PedidoEntero> get() = _pedido.asStateFlow()

    fun getPedido(id:Int){
        viewModelScope.launch (Dispatchers.IO){
           _pedido.value = dao.getPedido(id)
        }
    }

}