package com.example.practicaaaron.ui.viewModel

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.practicaaaron.BuildConfig
import com.example.practicaaaron.clases.basedatos.repositorio.UsuarioRepositorioOffline
import com.example.practicaaaron.clases.errores.ErrorLog
import com.example.practicaaaron.clases.usuarios.Data
import com.example.practicaaaron.clases.entidades.Usuario
import com.example.practicaaaron.clases.usuarios.UsuarioLogin
import com.example.practicaaaron.clases.utilidades.isInternetAvailable
import com.example.practicaaaron.repositorio.RepositorioRetrofit
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val dao: UsuarioRepositorioOffline
) : ViewModel() {

    @RequiresApi(Build.VERSION_CODES.O)
    private val repositorio: RepositorioRetrofit = RepositorioRetrofit()

    private val _isLogged = MutableStateFlow<Int?>(-1)
    val isLogged: StateFlow<Int?> get() = _isLogged.asStateFlow()

    private val _mensaje = MutableStateFlow("")
    val mensaje: StateFlow<String> get() = _mensaje.asStateFlow()

    private val _informacionUsuario = MutableStateFlow<Data?>(null)

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading.asStateFlow()

    fun borrarUser(){
        viewModelScope.launch(Dispatchers.IO) {
            dao.borrar()
        }
    }

    fun isLoading(){
        _isLoading.value = false
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun hacerLogin(usuarioLogin: UsuarioLogin,context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                if(isInternetAvailable(context)){
                    _isLoading.value = true
                    delay(3000)
                    _isLogged.value = -1

                    val response = repositorio.hacerLogin(usuarioLogin)
                    _informacionUsuario.value = response

                    if (_informacionUsuario.value?.dataUser?.tipoPerfil == 1) {
                        _isLogged.value = 1
                    } else if (_informacionUsuario.value?.dataUser?.tipoPerfil == 2) {
                        _isLogged.value = 2
                    }

                    _mensaje.value = ""

                    if (_informacionUsuario.value?.dataUser != null && _isLogged.value != -1) {
                        val user = Usuario(
                            _informacionUsuario.value?.dataUser?.idUsuario!!,
                            _informacionUsuario.value!!.dataUser.username,
                            _informacionUsuario.value!!.dataUser.idPerfil,
                            _informacionUsuario.value!!.dataUser.tipoPerfil,
                            _informacionUsuario.value!!.dataUser.nombre,
                            _informacionUsuario.value!!.dataUser.email
                        )

                        dao.insertarUsuario(user)
                    }

                    if (_isLogged.value == -1)
                        _mensaje.value = "Usuario o contraseña incorrectos"
                }else{
                    val mensaje = "No hay Internet. Compruebe su conexión"
                    _mensaje.value = mensaje
                }
            } catch (e: Exception) {
                _mensaje.value = "Algo ha fallado. Inténtelo de nuevo"
                viewModelScope.launch {
                    val err = ErrorLog(
                        ::hacerLogin.name,
                        "App",
                        "$e",
                        "",
                        _informacionUsuario.value?.dataUser?.idUsuario ?: 0,
                        BuildConfig.VERSION_CODE,
                        usuarioLogin.toString(),
                        LocalDate.now().toString()
                    )
                    repositorio.mandarError(err)
                }
            }
            _isLoading.value = false
        }
    }
}