package com.example.practicaaaron.ui.viewModel

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.practicaaaron.BuildConfig
import com.example.practicaaaron.clases.basedatos.repositorio.UsuarioRepositorioOffline
import com.example.practicaaaron.clases.errores.ErrorLog
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

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class OpcionesViewModel @Inject constructor(
    val dao:UsuarioRepositorioOffline
) : ViewModel() {

    private val repositorio: RepositorioRetrofit = RepositorioRetrofit()
    //Variables utilizadas en: Login
    // Valores: -1 si no hay usuario, 1 si es usuario, 2 si es admin
    private val _isLogged = MutableStateFlow<Int?>(-1)
    val isLogged: StateFlow<Int?> get() = _isLogged.asStateFlow()

    fun mandarCerrarSesion(context: Context){
        viewModelScope.launch(Dispatchers.IO){
            try {
                if(isInternetAvailable(context))
                    repositorio.cerrarSesion(dao.getId())
            }catch(e:Exception){
                val id = dao.getId()
                val err = ErrorLog("mandarCerrarSesion", "App", "$e", "",BuildConfig.VERSION_CODE,id,"", LocalDate.now().toString())
                if(isInternetAvailable(context))
                    repositorio.mandarError(err)
            }
        }
    }
}