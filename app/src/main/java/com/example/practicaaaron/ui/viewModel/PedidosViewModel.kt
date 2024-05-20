package com.example.practicaaaron.ui.viewModel

import android.content.Context
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.practicaaaron.clases.basedatos.repositorio.PedidosRepositorioOffline
import com.example.practicaaaron.clases.entidades.pedidos.Cliente
import com.example.practicaaaron.clases.entidades.pedidos.Direccion
import com.example.practicaaaron.clases.entidades.pedidos.Entrega
import com.example.practicaaaron.clases.entidades.pedidos.PCab
import com.example.practicaaaron.clases.entidades.pedidos.PLin
import com.example.practicaaaron.clases.entidades.pedidos.PedidoEntero
import com.example.practicaaaron.clases.pedidos.DataPedido
import com.example.practicaaaron.clases.pedidos.Informacion
import com.example.practicaaaron.clases.utilidades.isInternetAvailable
import com.example.practicaaaron.repositorio.RepositorioRetrofit
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.Base64
import java.util.stream.Collectors
import javax.inject.Inject

@HiltViewModel
class PedidosViewModel @Inject constructor(
    private val dao: PedidosRepositorioOffline
) :ViewModel(){

    @RequiresApi(Build.VERSION_CODES.O)
    private val repositorio: RepositorioRetrofit = RepositorioRetrofit()

    private val _pedidos = MutableStateFlow<List<PedidoEntero>>(listOf())
    val pedidos:StateFlow<List<PedidoEntero>> get() = _pedidos.asStateFlow()

    private val _informacion = MutableStateFlow(Informacion())
    val informacion:StateFlow<Informacion> get() = _informacion.asStateFlow()

    private val _loading = MutableStateFlow(true)
    val loading:StateFlow<Boolean> get() = _loading.asStateFlow()


    @RequiresApi(Build.VERSION_CODES.O)
    fun obtenerPedidos(id:Int, fecha: LocalDate, context: Context){
        viewModelScope.launch (Dispatchers.IO){
            _loading.value = true
            if(isInternetAvailable(context)){
                val entrega = repositorio.recuperarPedidos(id,fecha) ?: DataPedido()

                try{
                    if(entrega.data.retcode != -3){
                        val pedidos = entrega.data.pedidos

                        val pcabs:MutableList<PCab> = mutableListOf()
                        val clientes:MutableSet<Cliente> = mutableSetOf()
                        val bultos:MutableList<PLin> = mutableListOf()
                        val direcciones:MutableSet<Direccion> = mutableSetOf()
                        val entregas:MutableList<Entrega> = mutableListOf()

                        pedidos.stream().forEach {
                            pcabs.add(PCab(it.idPedido,it.fechaEntrega.toString(),if(it.entregado) 1 else 0,it.nombre,it.incidencia,it.latitud.toFloat(),it.altitud.toFloat(),it.descripcion,it.imagenDescripcion,id,it.cliente.idCliente,it.idEntrega,it.cliente.direccion.idDireccion))
                            clientes.add(Cliente(it.cliente.idCliente,it.cliente.dni,it.cliente.telefono,it.cliente.nombre))
                            it.bultos.stream().forEach { it2 ->
                                bultos.add(PLin(it2.idBulto,it2.refBulto,it2.descripcion,it2.unidades,it.idPedido))
                            }
                            direcciones.add(Direccion(it.cliente.direccion.idDireccion,it.cliente.direccion.tipoCalle,it.cliente.direccion.nombreCalle,it.cliente.direccion.portal
                                ,it.cliente.direccion.numero,it.cliente.direccion.poblacion,it.cliente.direccion.municipio,it.cliente.direccion.codigoPostal,it.cliente.idCliente))
                            entregas.add(Entrega(it.idEntrega,"","",0f,0f))
                        }

                        pcabs.stream().forEach { dao.insertarPedido(it) }
                        clientes.stream().forEach { dao.insertarCliente(it) }
                        bultos.stream().forEach { dao.insertarBultos(it) }
                        direcciones.stream().forEach { dao.insertarDireccion(it) }
                        entregas.stream().forEach { dao.insertarEntrega(it) }

                        _pedidos.value = dao.getPedidosHoy(id,fecha.toString()).stream().sorted { o1, o2 ->  o1.pedido.incidencia - o2.pedido.incidencia }.collect(Collectors.toList())
                        setInfo(_pedidos.value)

                    }else{
                        _pedidos.value = listOf()
                    }
                }catch (e:Exception){
                    Log.i("e", e.toString())
                    _pedidos.value = listOf()
                }
            }else{
                _pedidos.value = dao.getPedidosHoy(id,fecha.toString()).stream().sorted { o1, o2 -> o1.pedido.incidencia - o2.pedido.incidencia }.collect(Collectors.toList())
                setInfo(_pedidos.value)
            }
            _loading.value = false
        }
    }

    private fun setInfo(lista:List<PedidoEntero>){
        _informacion.value.pedidos = lista.count()
        _informacion.value.porEntregar = lista.stream().filter { it.pedido.incidencia == 0 }.count().toInt()
        _informacion.value.incidencia = lista.stream().filter { it.pedido.incidencia != 0 && it.pedido.incidencia != 100 }.count().toInt()
        _informacion.value.entregados = lista.stream().filter { it.pedido.incidencia == 100 }.count().toInt()
    }

    fun filtrar(texto:String,id:Int,fecha:LocalDate){
        viewModelScope.launch (Dispatchers.IO){
            _loading.value = true
            _pedidos.value = dao.getPedidosHoy(id,fecha.toString())
            _pedidos.value = _pedidos.value.stream().filter { it.pedido.nombre.contains(texto) }.sorted { o1, o2 ->  o1.pedido.incidencia - o2.pedido.incidencia }.collect(Collectors.toList())
            _loading.value = false
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun loadImageFromBase64(texto: String): ImageBitmap? {
    return try {
        val decodebytes = Base64.getDecoder().decode(texto)
        val bitmap = BitmapFactory.decodeByteArray(decodebytes, 0, decodebytes.size)
        bitmap.asImageBitmap()
    } catch (e: Exception) {
        null
    }
}