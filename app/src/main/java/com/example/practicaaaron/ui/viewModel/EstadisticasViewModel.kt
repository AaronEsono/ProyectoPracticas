package com.example.practicaaaron.ui.viewModel

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.practicaaaron.clases.basedatos.repositorio.EstadisticaRepositorioOffline
import com.example.practicaaaron.clases.entidades.EstadisticaUsuario
import com.example.practicaaaron.clases.utilidades.isInternetAvailable
import com.example.practicaaaron.repositorio.RepositorioRetrofit
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EstadisticasViewModel @Inject constructor(
    private val dao: EstadisticaRepositorioOffline
): ViewModel(){

    @RequiresApi(Build.VERSION_CODES.O)
    private val repositorio: RepositorioRetrofit = RepositorioRetrofit()

    private val _informacion = MutableStateFlow(EstadisticaUsuario(-1))
    val informacion: StateFlow<EstadisticaUsuario> get() = _informacion.asStateFlow()

    @RequiresApi(Build.VERSION_CODES.O)
    fun obtenerInformacion(id: Int,context:Context) {
        viewModelScope.launch(Dispatchers.IO) {

            if(isInternetAvailable(context)){
                val response = repositorio.resultadosTrabajadores(id)
                _informacion.value = EstadisticaUsuario(
                    id,
                    response.nombre.resultados.entregados,
                    response.nombre.resultados.incidencias,
                    response.nombre.resultados.sientregar,
                    response.nombre.porcentajes.pEntregados,
                    response.nombre.porcentajes.pIncidencias,
                    response.nombre.porcentajes.pSinEntregar,
                    response.nombre.pedidosTotales
                )
                dao.insertar(_informacion.value)
            }else{
                _informacion.value = dao.encontrar(id)
            }
        }
    }
}