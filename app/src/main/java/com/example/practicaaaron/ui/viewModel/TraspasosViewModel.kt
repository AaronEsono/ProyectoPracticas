package com.example.practicaaaron.ui.viewModel

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.practicaaaron.BuildConfig
import com.example.practicaaaron.R
import com.example.practicaaaron.clases.basedatos.repositorio.PedidosRepositorioOffline
import com.example.practicaaaron.clases.basedatos.repositorio.UsuarioRepositorioOffline
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
    private val eventosViewModel: EventosViewModel = EventosViewModel()
) : ViewModel() {

    @RequiresApi(Build.VERSION_CODES.O)
    private val repositorio: RepositorioRetrofit = RepositorioRetrofit()

    private val _pedidos = MutableStateFlow<List<PedidoEntero>>(mutableListOf())
    val pedidos: StateFlow<List<PedidoEntero>> get() = _pedidos.asStateFlow()

    @RequiresApi(Build.VERSION_CODES.O)
    fun getPedidosTraspasos(context: Context) {
        eventosViewModel.setState(EventosUIState.Cargando)
        try {
            viewModelScope.launch(Dispatchers.IO) {
                val id = uDao.getId()
                if (isInternetAvailable(context)) {
                    val entrega = repositorio.recuperarPedidos(id, LocalDate.now()) ?: DataPedido()

                    if (entrega.data.retcode != -3) {
                        val pedidos = entrega.data.pedidos

                        val pcabs: MutableList<PCab> = mutableListOf()
                        val clientes: MutableSet<Cliente> = mutableSetOf()
                        val bultos: MutableList<PLin> = mutableListOf()
                        val direcciones: MutableSet<Direccion> = mutableSetOf()
                        val entregas: MutableList<Entrega> = mutableListOf()

                        pedidos.stream().forEach {
                            pcabs.add(
                                PCab(
                                    it.idPedido,
                                    it.fechaEntrega.toString(),
                                    if (it.entregado) 1 else 0,
                                    it.nombre,
                                    it.incidencia,
                                    it.latitud.toFloat(),
                                    it.altitud.toFloat(),
                                    it.descripcion,
                                    it.imagenDescripcion,
                                    id,
                                    it.cliente.idCliente,
                                    it.idEntrega,
                                    it.cliente.direccion.idDireccion,
                                    false
                                )
                            )
                            clientes.add(
                                Cliente(
                                    it.cliente.idCliente,
                                    it.cliente.dni,
                                    it.cliente.telefono,
                                    it.cliente.nombre
                                )
                            )
                            it.bultos.stream().forEach { it2 ->
                                bultos.add(
                                    PLin(
                                        it2.idBulto,
                                        it2.refBulto,
                                        it2.descripcion,
                                        it2.unidades,
                                        it.idPedido
                                    )
                                )
                            }
                            direcciones.add(
                                Direccion(
                                    it.cliente.direccion.idDireccion,
                                    it.cliente.direccion.tipoCalle,
                                    it.cliente.direccion.nombreCalle,
                                    it.cliente.direccion.portal,
                                    it.cliente.direccion.numero,
                                    it.cliente.direccion.poblacion,
                                    it.cliente.direccion.municipio,
                                    it.cliente.direccion.codigoPostal,
                                    it.cliente.idCliente
                                )
                            )
                            entregas.add(Entrega(it.idEntrega, "", "", 0f, 0f))
                        }

                        pcabs.stream().forEach { dao.insertarPedido(it) }
                        clientes.stream().forEach { dao.insertarCliente(it) }
                        bultos.stream().forEach { dao.insertarBultos(it) }
                        direcciones.stream().forEach { dao.insertarDireccion(it) }
                        entregas.stream().forEach { dao.insertarEntrega(it) }

                        _pedidos.value = dao.getPedidosHoy(id, LocalDate.now().toString()).stream()
                            .filter { it.pedido.incidencia != 100 }
                            .sorted { o1, o2 -> o1.pedido.incidencia - o2.pedido.incidencia }
                            .collect(Collectors.toList())
                        eventosViewModel.setState(EventosUIState.Done)
                    } else {
                        eventosViewModel.setState(EventosUIState.Error(R.string.errorDatos))
                        _pedidos.value = listOf()
                        eventosViewModel.setState(EventosUIState.Done)
                    }
                } else {
                    val id = uDao.getId()
                    _pedidos.value = dao.pedidosNoHechos(id,LocalDate.now().toString())
                    _pedidos.value = _pedidos.value.stream().sorted { o1, o2 -> o1.pedido.incidencia - o2.pedido.incidencia }.collect(Collectors.toList())
                    eventosViewModel.setState(EventosUIState.Error(R.string.noConexion))
                }
            }
        } catch (e: Exception) {
            if (isInternetAvailable(context)) {
                val id2 = uDao.getId()
                val err = ErrorLog(
                    "obtenerPedidos",
                    "App",
                    "$e",
                    "",
                    id2,
                    BuildConfig.VERSION_CODE,
                    "",
                    LocalDate.now().toString()
                )
                viewModelScope.launch {
                    repositorio.mandarError(err)
                }
            }
            eventosViewModel.setState(EventosUIState.Error(R.string.algo))
        }
    }
}