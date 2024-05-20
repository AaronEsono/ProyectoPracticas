package com.example.practicaaaron.ui.viewModel

import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.util.Base64
import androidx.annotation.RequiresApi
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.practicaaaron.clases.basedatos.repositorio.PedidosRepositorioOffline
import com.example.practicaaaron.clases.basedatos.repositorio.UsuarioRepositorioOffline
import com.example.practicaaaron.clases.entidades.pedidos.Entrega
import com.example.practicaaaron.clases.utilidades.LocationService
import com.example.practicaaaron.clases.utilidades.isInternetAvailable
import com.example.practicaaaron.repositorio.RepositorioRetrofit
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import javax.inject.Inject

@HiltViewModel
class EntregaViewModel @Inject constructor(
    private val dao: PedidosRepositorioOffline,
    private val uDao: UsuarioRepositorioOffline
): ViewModel(){

    @RequiresApi(Build.VERSION_CODES.O)
    private val repositorio: RepositorioRetrofit = RepositorioRetrofit()

    private val _nombre = MutableStateFlow("")
    val nombre:StateFlow<String> get() = _nombre.asStateFlow()

    private val conseguirLocalizacion = LocationService()

    private val _entrega = MutableStateFlow(-1)
    val entrega :StateFlow<Int> get() = _entrega.asStateFlow()

    private val _id = MutableStateFlow(-1)
    val id:StateFlow<Int> get() = _id.asStateFlow()

    fun getName(id:Int){
        viewModelScope.launch (Dispatchers.IO){
            _nombre.value =  dao.darNombre(id)
        }
    }

    fun getId(){
        viewModelScope.launch (Dispatchers.IO){
            _id.value = uDao.getId()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun hacerEntrega(
        foto: Bitmap,
        valorBarras: String,
        content: Context,
        imageBitmap: ImageBitmap,
        idPedido:Int
    ) {
            val entrega = com.example.practicaaaron.clases.entrega.Entrega()
            val fotoBase64 = encodeImage(foto)
            val fotoFirma = encodeImage(imageBitmap.asAndroidBitmap())

            viewModelScope.launch (Dispatchers.IO){
                val resultado = conseguirLocalizacion.getUserLocation(content)
                val id = uDao.getId()
                val idEntrega = dao.getIdEntrega(idPedido)

                if (resultado != null) {
                    entrega.latitud = resultado.latitude.toFloat()
                    entrega.longitud = resultado.longitude.toFloat()
                }

                val entregar = Entrega(idEntrega,fotoBase64 ?: "",valorBarras,entrega.latitud,entrega.longitud,fotoFirma ?: "")
                dao.updateEntrega(entregar)
                dao.updatePedido(idPedido)

                if(isInternetAvailable(content)){
                    val entregaServer = com.example.practicaaaron.clases.entrega.Entrega(fotoBase64?:"",entrega.latitud,entrega.longitud,valorBarras,idPedido,fotoFirma?:"",id)
                    repositorio.hacerEntrega(entregaServer)
                }
                _entrega.value = dao.estanTodos(id)
            }
    }
}

fun encodeImage(bm: Bitmap): String? {
    val baos = ByteArrayOutputStream()
    bm.compress(Bitmap.CompressFormat.JPEG, 20, baos)
    val b = baos.toByteArray()
    return Base64.encodeToString(b, Base64.DEFAULT)
}