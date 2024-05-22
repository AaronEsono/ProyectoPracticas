package com.example.practicaaaron.ui.viewModel

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.practicaaaron.R
import com.example.practicaaaron.clases.basedatos.repositorio.PedidosRepositorioOffline
import com.example.practicaaaron.clases.basedatos.repositorio.UsuarioRepositorioOffline
import com.example.practicaaaron.clases.entidades.Usuario
import com.example.practicaaaron.clases.entidades.pedidos.PedidoEntero
import com.example.practicaaaron.clases.pedidos.PedidoActualizar
import com.example.practicaaaron.clases.utilidades.coloresIncidencias
import com.example.practicaaaron.clases.utilidades.isInternetAvailable
import com.example.practicaaaron.repositorio.RepositorioRetrofit
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class InfoProductoViewModel @Inject constructor(
    private val dao: PedidosRepositorioOffline,
    private val uDao: UsuarioRepositorioOffline,
    private val eventosViewModel: EventosViewModel = EventosViewModel()
) : ViewModel(){

    @RequiresApi(Build.VERSION_CODES.O)
    private val repositorio: RepositorioRetrofit = RepositorioRetrofit()

    private val _pedido = MutableStateFlow(PedidoEntero())
    val pedido:StateFlow<PedidoEntero> get() = _pedido.asStateFlow()

    private val _tipoPerfil = MutableStateFlow(Usuario())
    val tipoPerfil:StateFlow<Usuario> get() = _tipoPerfil.asStateFlow()

    private val _entrega = MutableStateFlow(-1)
    val entrega :StateFlow<Int> get() = _entrega.asStateFlow()

    fun getPedido(id:Int){
        viewModelScope.launch (Dispatchers.IO){
           _pedido.value = dao.getPedido(id)
        }
    }

    fun getTipoPerfil(){
        viewModelScope.launch(Dispatchers.IO){
            _tipoPerfil.value = uDao.getUsuario()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun actualizarIncidencia(valor: String, context:Context,fecha:LocalDate) {
        viewModelScope.launch(Dispatchers.IO) {
            try{
                eventosViewModel.setState(EventosUIState.Cargando)
                val incidencia: Int = coloresIncidencias.stream().filter { it.nombre == valor }.findFirst().map { it.incidencia }.get()
                val idUsuario = uDao.getId()

                if(isInternetAvailable(context)){
                    repositorio.actualizarPedido(PedidoActualizar(_pedido.value.pedido.idPedido, incidencia, idUsuario))
                    eventosViewModel.setState(EventosUIState.Success(R.string.textoIncidencia,R.string.incidencia,fecha,idUsuario))
                    dao.actualizarIncidencia(incidencia, _pedido.value.pedido.idPedido)
                }else{
                    dao.actualizarIncidenciaOffline(incidencia, _pedido.value.pedido.idPedido)
                    eventosViewModel.setState(EventosUIState.Error(R.string.errorIncidencia))
                }
                _entrega.value = dao.estanTodos(idUsuario)
            }catch(e:Exception){
                Log.i("error", e.toString())
                eventosViewModel.setState(EventosUIState.Error(-1))
            }
        }
    }
}