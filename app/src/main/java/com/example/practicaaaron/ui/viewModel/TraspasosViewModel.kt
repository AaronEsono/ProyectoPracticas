package com.example.practicaaaron.ui.viewModel

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.practicaaaron.BuildConfig
import com.example.practicaaaron.R
import com.example.practicaaaron.clases.basedatos.modulos.ModelMapperInstance
import com.example.practicaaaron.clases.basedatos.repositorio.PedidosRepositorioOffline
import com.example.practicaaaron.clases.basedatos.repositorio.TraspasosRepositorioOffline
import com.example.practicaaaron.clases.basedatos.repositorio.UsuarioRepositorioOffline
import com.example.practicaaaron.clases.entidades.TransferirPedido
import com.example.practicaaaron.clases.entidades.pedidos.Cliente
import com.example.practicaaaron.clases.entidades.pedidos.Direccion
import com.example.practicaaaron.clases.entidades.pedidos.Entrega
import com.example.practicaaaron.clases.entidades.pedidos.PCab
import com.example.practicaaaron.clases.entidades.pedidos.PLin
import com.example.practicaaaron.clases.entidades.pedidos.PedidoEntero
import com.example.practicaaaron.clases.errores.ErrorLog
import com.example.practicaaaron.clases.pedidos.DataPedido
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
class TraspasosViewModel @Inject constructor(
    private val dao: PedidosRepositorioOffline,
    private val uDao: UsuarioRepositorioOffline,
    private val tDao:TraspasosRepositorioOffline,
    private val eventosViewModel: EventosViewModel = EventosViewModel()
) : ViewModel() {

    @RequiresApi(Build.VERSION_CODES.O)
    private val repositorio: RepositorioRetrofit = RepositorioRetrofit()

    private val _pedidos = MutableStateFlow<List<PedidoEntero>>(mutableListOf())
    val pedidos: StateFlow<List<PedidoEntero>> get() = _pedidos.asStateFlow()

    private val _pedidosMarcados = MutableStateFlow<MutableList<Int>>(mutableListOf())

    private val _cuantos = MutableStateFlow(0)
    val cuantos:StateFlow<Int> get() = _cuantos.asStateFlow()

    private val _guardado = MutableStateFlow(false)
    val guardado:StateFlow<Boolean> get() = _guardado.asStateFlow()

    @RequiresApi(Build.VERSION_CODES.O)
    fun getPedidosTraspasos(context: Context) {
        eventosViewModel.setState(EventosUIState.Cargando)
        try {
            viewModelScope.launch(Dispatchers.IO) {
                val id = uDao.getId()
                if (isInternetAvailable(context)) {
                    val entrega = repositorio.recuperarPedidos(id, LocalDate.now()) ?: DataPedido()

                    if (entrega.data.pedidos != null) {
                        val pedidos = entrega.data.pedidos

                        val pcabs: MutableList<PCab> = mutableListOf()
                        val clientes: MutableSet<Cliente> = mutableSetOf()
                        val bultos: MutableList<PLin> = mutableListOf()
                        val direcciones: MutableSet<Direccion> = mutableSetOf()
                        val entregas: MutableList<Entrega> = mutableListOf()

                        pedidos.stream().forEach {
                            val modelMapper = ModelMapperInstance()
                            val pedido = modelMapper.map(it,PCab::class.java)
                            pedido.idUsuario = id
                            pedido.incidencia = it.incidencia
                            pedido.idCliente = it.cliente.idCliente
                            pedido.idDireccion = it.cliente.direccion.idDireccion

                            pcabs.add(pedido)

                            clientes.add(modelMapper.map(it.cliente,Cliente::class.java))

                            it.bultos.stream().forEach { it2 ->
                                val bulto = modelMapper.map(it2,PLin::class.java)
                                bulto.idPedido = it.idPedido
                                bultos.add(bulto)
                            }

                            val direccion = modelMapper.map(it.cliente.direccion,Direccion::class.java)
                            direccion.idCliente = it.cliente.idCliente
                            direcciones.add(direccion)

                            entregas.add(Entrega(it.idEntrega, "", "", 0f, 0f))
                        }

                        pcabs.stream().forEach { dao.insertarPedido(it) }
                        clientes.stream().forEach { dao.insertarCliente(it) }
                        bultos.stream().forEach { dao.insertarBultos(it) }
                        direcciones.stream().forEach { dao.insertarDireccion(it) }
                        entregas.stream().forEach { dao.insertarEntrega(it) }

                        _pedidos.value = dao.getPedidosHoy(id, LocalDate.now().toString()).stream().filter { it.pedido.incidencia != 100 }.sorted { o1, o2 -> o1.pedido.incidencia - o2.pedido.incidencia }.collect(Collectors.toList())
                        eventosViewModel.setState(EventosUIState.Done)
                    } else {
                        _pedidos.value = listOf()
                        eventosViewModel.setState(EventosUIState.Error(R.string.noPedidos2))
                    }
                } else {
                    _pedidos.value = dao.pedidosNoHechos(id,LocalDate.now().toString())
                    _pedidos.value = _pedidos.value.stream().sorted { o1, o2 -> o1.pedido.incidencia - o2.pedido.incidencia }.collect(Collectors.toList())
                    eventosViewModel.setState(EventosUIState.Error(R.string.noConexion))
                }
            }
        } catch (e: Exception) {
            if (isInternetAvailable(context)) {
                val id2 = uDao.getId()
                val err = ErrorLog("obtenerPedidos","App","$e","",id2,BuildConfig.VERSION_CODE,"",LocalDate.now().toString())
                viewModelScope.launch {
                    repositorio.mandarError(err)
                }
            }
            eventosViewModel.setState(EventosUIState.Error(R.string.algo))
        }
    }

    fun aniadirPedido(id:Int){
        _pedidosMarcados.value.add(id)
        _cuantos.value = _pedidosMarcados.value.size
    }

    fun eliminarPedido(id:Int){
        _pedidosMarcados.value.remove(id)
        _cuantos.value = _pedidosMarcados.value.size
    }

    fun borrarTraspasosDesechos(){
        viewModelScope.launch (Dispatchers.IO){
            tDao.borrarHechos()
        }
    }

    fun guardarDatos(){
        viewModelScope.launch (Dispatchers.IO){
            eventosViewModel.setState(EventosUIState.Cargando)
            val traspasos = mutableListOf<TransferirPedido>()
            val id = uDao.getId()

            _pedidosMarcados.value.forEach {
                traspasos.add(TransferirPedido(id,it,-1,false))
            }

            traspasos.forEach {
                tDao.insertar(it)
            }
            _guardado.value = true
        }
    }

}


