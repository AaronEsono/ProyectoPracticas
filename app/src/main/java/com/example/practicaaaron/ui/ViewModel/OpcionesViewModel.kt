package com.example.practicaaaron.ui.ViewModel

import android.graphics.Bitmap
import android.os.Build
import android.util.Base64
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.practicaaaron.clases.entrega.Entrega
import com.example.practicaaaron.clases.incidencias.ColoresIncidencias
import com.example.practicaaaron.clases.pedidos.DataPedido
import com.example.practicaaaron.clases.pedidos.PedidoActualizar
import com.example.practicaaaron.clases.pedidos.PedidoCab
import com.example.practicaaaron.clases.usuarios.Data
import com.example.practicaaaron.clases.usuarios.UsuarioLogin
import com.example.practicaaaron.repositorio.RepositorioRetrofit
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream

@RequiresApi(Build.VERSION_CODES.O)
class OpcionesViewModel(
    private val repositorio: RepositorioRetrofit = RepositorioRetrofit()
):ViewModel() {
    private val _pedidosRepartidor = MutableStateFlow<DataPedido?>(null)
    private val _informacionUsuario = MutableStateFlow<Data?>(null)
    private val _isLogged = MutableStateFlow<Boolean?>(false)
    private val  _mensaje = MutableStateFlow<String?>("")
    private val _pedido = MutableStateFlow<PedidoCab?>(PedidoCab())

    val pedidosRepartidor: StateFlow<DataPedido?> get() = _pedidosRepartidor.asStateFlow()
    val informacionUsuario: StateFlow<Data?> get() = _informacionUsuario.asStateFlow()
    val isLogged: StateFlow<Boolean?> get() = _isLogged.asStateFlow()
    val mensaje: StateFlow<String?> get() = _mensaje.asStateFlow()
    val pedido: StateFlow<PedidoCab?> get() = _pedido.asStateFlow()

    val coloresIncidencias = listOf(
        ColoresIncidencias(0xFFcf8ac6,0,"Normal"),
        ColoresIncidencias(0xFF1fff87,100,"Entregado"),
        ColoresIncidencias(0xFFf7941b,10,"Ausente"),
        ColoresIncidencias(0xFFf7351b,20,"Pérdida"),
        ColoresIncidencias(0xFF3492eb,30,"Rechazo"),
        ColoresIncidencias(0xFFf71b56,40,"dirección errónea")
    )

    fun obtenerPedidos(){
        viewModelScope.launch {
            val response = informacionUsuario.value?.dataUser?.let { repositorio.recuperarPedidos(it.idUsuario) }
            _pedidosRepartidor.value = response
        }
    }

    fun hacerLogin(usuarioLogin: UsuarioLogin) {
        viewModelScope.launch {
            val response = repositorio.hacerLogin(usuarioLogin)
            _informacionUsuario.value = response
            _isLogged.value = _informacionUsuario.value != null
            _mensaje.value = ""

            if(_isLogged.value == false)
                _mensaje.value = "Usuario o contraseña incorrectos"
        }
    }

    fun obtenerPedido(pedidoCab: PedidoCab){
        viewModelScope.launch {
            _pedido.value = pedidoCab
        }
    }

    fun cerrarSesion(){
        _informacionUsuario.value = null
        _isLogged.value = false
        _pedido.value = null
        _pedidosRepartidor.value = null
    }

    fun indicarColorPedido(incidencia:Int):Long{
        var colorLong:Long = 0xFFcf8ac6

        coloresIncidencias.forEach{
            if(it.incidencia == incidencia){
                colorLong = it.color
            }
        }
        return colorLong
    }

    fun actualizarPedido(valorIncidencia: String?) {
        var pedido = PedidoActualizar(_pedido.value?.idPedido?: 1,getIntIncidencia(valorIncidencia?:""))

        viewModelScope.launch {
            repositorio.actualizarPedido(pedido)
        }
    }

    fun getIntIncidencia(valor:String):Int{
        var defecto:Int = 30
        Log.i("incidencia",valor)

        coloresIncidencias.forEach {
            if(it.nombre == valor)
                defecto = it.incidencia
        }

        return defecto
    }

    //Falta la longitud y altitud
    fun hacerEntrega(foto:Bitmap,valorBarras:String){
        var entrega = Entrega()
        var fotoBase64 = encodeImage(foto)

        entrega.idPedido = _pedido.value?.idPedido?:0
        entrega.lecturaBarcode = valorBarras
        entrega.fotoEntrega = fotoBase64?:""

        viewModelScope.launch {
            repositorio.hacerEntrega(entrega)
        }
    }
}

private fun encodeImage(bm: Bitmap): String? {
    val baos = ByteArrayOutputStream()
    bm.compress(Bitmap.CompressFormat.JPEG, 20, baos)
    val b = baos.toByteArray()
    return Base64.encodeToString(b, Base64.DEFAULT)
}