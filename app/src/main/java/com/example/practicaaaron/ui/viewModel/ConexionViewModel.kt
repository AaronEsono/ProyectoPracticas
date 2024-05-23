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
import com.example.practicaaaron.clases.entrega.Entrega
import com.example.practicaaaron.clases.errores.ErrorLog
import com.example.practicaaaron.clases.utilidades.isInternetAvailable
import com.example.practicaaaron.repositorio.RepositorioRetrofit
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class ConexionViewModel @Inject constructor(
    private val pDao: PedidosRepositorioOffline,
    private val uDao: UsuarioRepositorioOffline,
    private val eventosViewModel: EventosViewModel = EventosViewModel()
):ViewModel(){

    @RequiresApi(Build.VERSION_CODES.O)
    private val repositorio: RepositorioRetrofit = RepositorioRetrofit()

    @RequiresApi(Build.VERSION_CODES.O)
    fun mandarEnLocal(context:Context) {
        viewModelScope.launch (Dispatchers.IO){
            val pedidos = pDao.entregasEnLocal()
            val pedidosEntrega: MutableList<Entrega> = mutableListOf()

            if (pedidos.isNotEmpty()) {
                try {
                    pedidos.forEach {
                        pedidosEntrega.add(Entrega(it.fotoEntrega,it.latitud,it.altitud,it.codigoBarras,1,it.firma,1))
                    }

                    pedidosEntrega.forEach {
                        if(isInternetAvailable(context))
                            repositorio.hacerEntrega(it)
                    }
                }catch (e:Exception){
                    if(isInternetAvailable(context)){
                        val id2 = uDao.getId()
                        val err = ErrorLog("mandarEnLocal", "App", "$e", "", id2, BuildConfig.VERSION_CODE,"$context", LocalDate.now().toString())
                        if(isInternetAvailable(context))
                            repositorio.mandarError(err)
                    }
                    eventosViewModel.setState(EventosUIState.Error(R.string.algo))
                }
            }
        }
    }
}