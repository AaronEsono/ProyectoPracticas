package com.example.practicaaaron.ui.ViewModel

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.provider.ContactsContract
import android.util.Base64
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AcUnit
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.ArrowOutward
import androidx.compose.material.icons.rounded.Block
import androidx.compose.material.icons.rounded.CheckCircleOutline
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.Commit
import androidx.compose.material.icons.rounded.Commute
import androidx.compose.material.icons.rounded.LocalPrintshop
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.practicaaaron.R
import com.example.practicaaaron.clases.entrega.Entrega
import com.example.practicaaaron.clases.incidencias.ColoresIncidencias
import com.example.practicaaaron.clases.incidencias.Entregado
import com.example.practicaaaron.clases.pedidos.DataPedido
import com.example.practicaaaron.clases.pedidos.Informacion
import com.example.practicaaaron.clases.pedidos.PedidoActualizar
import com.example.practicaaaron.clases.pedidos.PedidoCab
import com.example.practicaaaron.clases.pedidos.Pedidos
import com.example.practicaaaron.clases.resultados.InformacionUsuarios
import com.example.practicaaaron.clases.resultados.Respuesta
import com.example.practicaaaron.clases.ubicaciones.Ubicacion
import com.example.practicaaaron.clases.usuarios.Data
import com.example.practicaaaron.clases.usuarios.UsuarioLogin
import com.example.practicaaaron.clases.usuarios.Usuarios
import com.example.practicaaaron.clases.utilidades.LocationService
import com.example.practicaaaron.repositorio.RepositorioRetrofit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.time.LocalDate
import java.util.stream.Collectors
import kotlin.streams.toList

@RequiresApi(Build.VERSION_CODES.O)
class OpcionesViewModel(
    private val repositorio: RepositorioRetrofit = RepositorioRetrofit()
) : ViewModel() {
    //Variables utilizadas en: Login
    // Valores: -1 si no hay usuario, 1 si es usuario, 2 si es admin
    private val _isLogged = MutableStateFlow<Int?>(-1)
    val isLogged: StateFlow<Int?> get() = _isLogged.asStateFlow()

    private val _mensaje = MutableStateFlow<String?>("")
    val mensaje: StateFlow<String?> get() = _mensaje.asStateFlow()

    private val _informacionUsuario = MutableStateFlow<Data?>(null)
    val informacionUsuario: StateFlow<Data?> get() = _informacionUsuario.asStateFlow()

    //Variables utilizadas en: Pantallas Pedidos
    private val _informacion = MutableStateFlow<Informacion>(Informacion())
    val informacion:StateFlow<Informacion> get() = _informacion.asStateFlow()

    private val _done = MutableStateFlow<Boolean>(false)
    val done:StateFlow<Boolean> get() = _done.asStateFlow()

    private val _pedidosRepartidor = MutableStateFlow<DataPedido?>(null)
    val pedidosRepartidor get() = _pedidosRepartidor.asStateFlow()

    private val _pedidosRepartidorCopy = MutableStateFlow<DataPedido?>(DataPedido())
    val pedidosRepartidorCopy get() = _pedidosRepartidorCopy.asStateFlow()

    private val _textoBusca = MutableStateFlow<String>("")
    val textoBusca get() = _textoBusca.asStateFlow()

    //Variables utilizadas en: PantallaInfoProducto
    private val _entregado = MutableStateFlow<Entregado?>(Entregado())
    val entregado:StateFlow<Entregado?> get() = _entregado.asStateFlow()

    private val _pedido = MutableStateFlow<PedidoCab?>(PedidoCab())
    val pedido: StateFlow<PedidoCab?> get() = _pedido.asStateFlow()

    private val _fecha = MutableStateFlow<LocalDate>(LocalDate.now())
    val fecha get() = _fecha.asStateFlow()

    //Variables utilizadas en: PantallaMapa
    private val _ubicaciones = MutableStateFlow<MutableList<Ubicacion>>(mutableListOf())
    val ubicaciones: StateFlow<MutableList<Ubicacion>> get() = _ubicaciones.asStateFlow()

    //Variables utilizadas en: PantallaUsuarios
    private val _usuarios = MutableStateFlow<Usuarios?>(null)
    val usuarios: StateFlow<Usuarios?> get() = _usuarios.asStateFlow()

    private val _esConsulta = MutableStateFlow(true)
    val esConsulta get() = _esConsulta.asStateFlow()

    //Variables utilizadas en: PantallaEstadisticas
    private val _idUsuarioAdmin = MutableStateFlow<Int>(0)
    val idUsuarioAdmin:StateFlow<Int> get() = _idUsuarioAdmin.asStateFlow()

    private val _resultadosTrabajadores = MutableStateFlow<Respuesta>(Respuesta())
    val resultadosTrabajadores:StateFlow<Respuesta> get() = _resultadosTrabajadores.asStateFlow()


    private val conseguirLocalizacion = LocationService()

    private val coloresIncidencias = listOf(
        ColoresIncidencias(Icons.Rounded.Commute, 0, "Por entregar", "Normal"),
        ColoresIncidencias(Icons.Rounded.CheckCircleOutline, 100, "Entregado", "Entregado"),
        ColoresIncidencias(Icons.Rounded.Block, 10, "Perdido", "Pérdida"),
        ColoresIncidencias(Icons.Rounded.Clear, 30, "Rechazo", "Rechazo"),
        ColoresIncidencias(Icons.Rounded.AcUnit, 20, "Ausente", "Ausente"),
        ColoresIncidencias(Icons.Rounded.AccountCircle,40,"Dirección errónea", "dirección errónea"))

    fun setId(id:Int){
        _idUsuarioAdmin.value = id
    }

    fun setEstadistica(estado:Boolean){
        _esConsulta.value = estado
    }

    //Obtener aqui todas las latitudes y altitudes
    fun obtenerPedidos() {
        viewModelScope.launch(Dispatchers.IO){
            try {
                val response =
                    informacionUsuario.value?.dataUser?.let { repositorio.recuperarPedidos(it.idUsuario,fecha.value) }
                _pedidosRepartidor.value = response

                if(response?.data?.pedidos != null){
                    response.data.pedidos = response.data.pedidos.stream().sorted { o1, o2 -> o1.incidencia - o2.incidencia }?.collect(Collectors.toList())!!
                    _pedidosRepartidorCopy.value?.data?.pedidos = response.data.pedidos
                }else{
                    _pedidosRepartidorCopy.value?.data?.pedidos = listOf()
                }

                setInfo(response)

                _pedidosRepartidor.value?.data?.pedidos?.forEach {
                    _ubicaciones.value.add(
                        Ubicacion(
                            it.latitud.toDouble(),
                            it.altitud.toDouble(),
                            it.nombre
                        )
                    )
                }
                _done.value = true
            }catch(e:Exception){
                _pedidosRepartidor.value = DataPedido()
                _pedidosRepartidorCopy.value = DataPedido()
            }
        }
    }

    fun setInfo(response: DataPedido?) {
        if(response?.data?.pedidos != null){
            _informacion.value.pedidos = response.data.pedidos.size
            _informacion.value.porEntregar = response.data.pedidos.stream().filter{it.incidencia == 0}.count().toInt()
            _informacion.value.entregados = response.data.pedidos.stream().filter{it.incidencia == 100}.count().toInt()
            _informacion.value.incidencia = response.data.pedidos.stream().filter{it.incidencia != 100 && it.incidencia != 0}.count().toInt()
        }else{
            _informacion.value.pedidos = 0
            _informacion.value.porEntregar = 0
            _informacion.value.entregados = 0
            _informacion.value.incidencia = 0
        }
    }

    fun setFecha(fecha:LocalDate){
        _fecha.value = fecha
    }

    fun setTexto(texto:String){
        _textoBusca.value = texto
        try{
            if(textoBusca.value != ""){
                _pedidosRepartidorCopy.value?.data?.pedidos = _pedidosRepartidor.value?.data?.copy()?.pedidos?.stream()?.filter { it.nombre.contains(_textoBusca.value) }?.collect(Collectors.toList())!!
                Log.i("veamos","${_pedidosRepartidorCopy.value?.data?.pedidos}")
                Log.i("veamos2","${_pedidosRepartidor.value?.data?.pedidos}")
            }
            else
                _pedidosRepartidorCopy.value?.data?.pedidos = _pedidosRepartidor.value?.data?.copy()?.pedidos!!
        }catch (e:Exception){
            _pedidosRepartidorCopy.value = DataPedido()
        }

    }

    fun obtenerPedidos(id: Int) {
        viewModelScope.launch(Dispatchers.IO){
            try{
                val response =
                    informacionUsuario.value?.dataUser?.let { repositorio.recuperarPedidos(id,LocalDate.now()) }
                _pedidosRepartidor.value = response

                if(response?.data?.pedidos != null){
                    response.data.pedidos = response.data.pedidos.stream().sorted { o1, o2 -> o1.incidencia - o2.incidencia }?.collect(Collectors.toList())!!
                    _pedidosRepartidorCopy.value?.data?.pedidos = response.data.pedidos
                }else{
                    _pedidosRepartidorCopy.value?.data?.pedidos = listOf()
                }

                _pedidosRepartidor.value?.data?.pedidos?.forEach {
                    _ubicaciones.value.add(
                        Ubicacion(
                            it.latitud.toDouble(),
                            it.altitud.toDouble(),
                            it.nombre
                        )
                    )
                }
                setInfo(response)
            }catch(e:Exception){
                _pedidosRepartidor.value = DataPedido()
                _pedidosRepartidorCopy.value = DataPedido()
            }
        }
        _done.value = true
    }


    fun hacerLogin(usuarioLogin: UsuarioLogin) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _isLogged.value = -1
                val response = repositorio.hacerLogin(usuarioLogin)
                _informacionUsuario.value = response

                if (_informacionUsuario.value?.dataUser?.tipoPerfil == 1) {
                    _isLogged.value = 1
                } else if (_informacionUsuario.value?.dataUser?.tipoPerfil == 2) {
                    _isLogged.value = 2
                }

                _mensaje.value = ""

                if (_isLogged.value == -1)
                    _mensaje.value = "Usuario o contraseña incorrectos"
            }catch (e:Exception){
                _mensaje.value = "Algo ha fallado. Por favor, pruebe de nuevo."
            }
        }
    }

    fun obtenerPedido(pedidoCab: PedidoCab) {
        viewModelScope.launch (Dispatchers.IO){
            _pedido.value = pedidoCab
        }
    }

    fun cerrarSesion() {
        _informacionUsuario.value = null
        _isLogged.value = -1
        _pedido.value = null
        _pedidosRepartidor.value = null
    }

    fun indicarColorPedido(incidencia: Int): ColoresIncidencias {
        return coloresIncidencias.stream().filter { it.incidencia == incidencia }.findFirst().orElse(ColoresIncidencias())
    }

    fun actualizarPedido(valorIncidencia: String?) {
        //Poner evento para controlar lo que se envia
        var pedido = PedidoActualizar(_pedido.value?.idPedido ?: 1, getIntIncidencia(valorIncidencia ?: ""))

        viewModelScope.launch(Dispatchers.IO){
            val response = repositorio.actualizarPedido(pedido)
            _entregado.value = response
        }
    }

    fun getIntIncidencia(valor: String): Int {
        return coloresIncidencias.stream().filter { it.nombre == valor }.findFirst().get().incidencia
    }

    //Falta la longitud y altitud
    fun hacerEntrega(
        foto: Bitmap,
        valorBarras: String,
        content: Context,
        imageBitmap: ImageBitmap
    ) {
        var entrega = Entrega()
        var fotoBase64 = encodeImage(foto)
        var fotoFirma = encodeImage(imageBitmap.asAndroidBitmap())

        entrega.idPedido = _pedido.value?.idPedido ?: 0
        entrega.lecturaBarcode = valorBarras ?: ""
        entrega.fotoEntrega = fotoBase64 ?: ""
        entrega.firma = fotoFirma ?: ""

        viewModelScope.launch (Dispatchers.IO){
            val resultado = conseguirLocalizacion.getUserLocation(content)

            if (resultado != null) {
                entrega.latitud = resultado.latitude.toFloat()
                entrega.longitud = resultado.longitude.toFloat()
            }

            val response:Entregado = repositorio.hacerEntrega(entrega)
            _entregado.value = response
        }
    }

    fun resetearEntrega(){
        _entregado.value?.retcode = -2
    }

    fun obtenerTodos() {
        viewModelScope.launch (Dispatchers.IO){
            val response = repositorio.obtenerTodos()
            _usuarios.value = response
        }
    }

    fun resultadosTrabajadores(){
        viewModelScope.launch (Dispatchers.IO){
            _resultadosTrabajadores.value = repositorio.resultadosTrabajadores(_idUsuarioAdmin.value)
        }
    }

}

private fun encodeImage(bm: Bitmap): String? {
    if(bm != null){
        val baos = ByteArrayOutputStream()
        bm.compress(Bitmap.CompressFormat.JPEG, 20, baos)
        val b = baos.toByteArray()
        return Base64.encodeToString(b, Base64.DEFAULT)
    }else
        return null
}