package com.example.practicaaaron.ui.ViewModel

import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.util.Base64
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.practicaaaron.R
import com.example.practicaaaron.clases.entrega.Entrega
import com.example.practicaaaron.clases.incidencias.ColoresIncidencias
import com.example.practicaaaron.clases.pedidos.DataPedido
import com.example.practicaaaron.clases.pedidos.PedidoActualizar
import com.example.practicaaaron.clases.pedidos.PedidoCab
import com.example.practicaaaron.clases.ubicaciones.Ubicacion
import com.example.practicaaaron.clases.usuarios.Data
import com.example.practicaaaron.clases.usuarios.UsuarioLogin
import com.example.practicaaaron.clases.utilidades.LocationService
import com.example.practicaaaron.repositorio.RepositorioRetrofit
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
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
    private val _ubicaciones = MutableStateFlow<MutableList<Ubicacion>>(mutableListOf())

    val pedidosRepartidor: StateFlow<DataPedido?> get() = _pedidosRepartidor.asStateFlow()
    val informacionUsuario: StateFlow<Data?> get() = _informacionUsuario.asStateFlow()
    val isLogged: StateFlow<Boolean?> get() = _isLogged.asStateFlow()
    val mensaje: StateFlow<String?> get() = _mensaje.asStateFlow()
    val pedido: StateFlow<PedidoCab?> get() = _pedido.asStateFlow()
    val ubicaciones:StateFlow<MutableList<Ubicacion>> get() = _ubicaciones.asStateFlow()

    val conseguirLocalizacion = LocationService()

    val coloresIncidencias = listOf(
        ColoresIncidencias(R.drawable.delivery,0,"Por entregar","Normal"),
        ColoresIncidencias(R.drawable.check,100,"Entregado","Entregado"),
        ColoresIncidencias(R.drawable.ausente,10,"Ausente","Ausente"),
        ColoresIncidencias(R.drawable.perdido,20,"Perdido","Pérdida"),
        ColoresIncidencias(R.drawable.rechazo,30,"Rechazo","Rechazo"),
        ColoresIncidencias(R.drawable.direccionerronea,40,"Dirección errónea","dirección errónea")
    )

    //Obtener aqui todas las latitudes y altitudes
    fun obtenerPedidos(){
        viewModelScope.launch {
            val response = informacionUsuario.value?.dataUser?.let { repositorio.recuperarPedidos(it.idUsuario) }
            _pedidosRepartidor.value = response

            Log.i("hh","${_pedidosRepartidor.value}")
            _pedidosRepartidor.value?.data?.pedidos?.forEach{
                _ubicaciones.value.add(Ubicacion(it.latitud.toDouble(),it.altitud.toDouble(),it.nombre))
            }
            Log.i("hh","${_ubicaciones.value}")
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

    fun indicarColorPedido(incidencia:Int):ColoresIncidencias{
        var colorLong:ColoresIncidencias = ColoresIncidencias()

        coloresIncidencias.forEach{
            if(it.incidencia == incidencia){
                colorLong = it
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
    fun hacerEntrega(foto: Bitmap, valorBarras: String, content: Context, imageBitmap: ImageBitmap){
        var entrega = Entrega()
        var fotoBase64 = encodeImage(foto)
        var fotoFirma = encodeImage(imageBitmap.asAndroidBitmap())

        entrega.idPedido = _pedido.value?.idPedido?:0
        entrega.lecturaBarcode = valorBarras
        entrega.fotoEntrega = fotoBase64?:""
        entrega.firma = fotoFirma?:""

        viewModelScope.launch {
            val resultado = conseguirLocalizacion.getUserLocation(content)

            if(resultado != null){
                entrega.latitud = resultado.latitude.toFloat()
                entrega.longitud = resultado.longitude.toFloat()

                Log.i("latitud","${entrega.latitud}")
                Log.i("longitud","${entrega.longitud}")
            }

            //repositorio.hacerEntrega(entrega)
        }
    }
}

private fun encodeImage(bm: Bitmap): String? {
    val baos = ByteArrayOutputStream()
    bm.compress(Bitmap.CompressFormat.JPEG, 20, baos)
    val b = baos.toByteArray()
    return Base64.encodeToString(b, Base64.DEFAULT)
}