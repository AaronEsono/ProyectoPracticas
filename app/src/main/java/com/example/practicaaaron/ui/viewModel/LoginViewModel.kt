package com.example.practicaaaron.ui.viewModel

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.practicaaaron.BuildConfig
import com.example.practicaaaron.R
import com.example.practicaaaron.clases.basedatos.repositorio.DataUsuarioRepositorioOffline
import com.example.practicaaaron.clases.basedatos.repositorio.EstadisticaRepositorioOffline
import com.example.practicaaaron.clases.basedatos.repositorio.PedidosRepositorioOffline
import com.example.practicaaaron.clases.basedatos.repositorio.UsuarioRepositorioOffline
import com.example.practicaaaron.clases.entidades.Usuario
import com.example.practicaaaron.clases.errores.ErrorLog
import com.example.practicaaaron.clases.pedidos.Perfiles
import com.example.practicaaaron.clases.usuarios.Data
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
    private val dao: UsuarioRepositorioOffline,
    private val pDao: PedidosRepositorioOffline,
    private val uDao: DataUsuarioRepositorioOffline,
    private val eDao: EstadisticaRepositorioOffline,
    private val eventosViewModel: EventosViewModel = EventosViewModel()
) : ViewModel() {

    @RequiresApi(Build.VERSION_CODES.O)
    private val repositorio: RepositorioRetrofit = RepositorioRetrofit()

    private val _isLogged = MutableStateFlow<Int?>(-1)
    val isLogged: StateFlow<Int?> get() = _isLogged.asStateFlow()

    private val _informacionUsuario = MutableStateFlow<Data?>(null)

    fun borrarDatos(){
        viewModelScope.launch (Dispatchers.IO){
            pDao.borrarEntregas()
            pDao.borrarPedidos()
            pDao.borrarBultos()
            pDao.borrarClientes()
            pDao.borrarDireccion()
            dao.borrar()
            uDao.borrarTodos()
            eDao.borrar()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun hacerLogin(usuarioLogin: UsuarioLogin,context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                eventosViewModel.setState(EventosUIState.Cargando)
                if(isInternetAvailable(context)){
                    delay(3000)
                    _isLogged.value = -1

                    val response = repositorio.hacerLogin(usuarioLogin)
                    _informacionUsuario.value = response

                    if (_informacionUsuario.value?.dataUser?.tipoPerfil == Perfiles.USUARIO.num) {
                        _isLogged.value = Perfiles.USUARIO.num
                    } else if (_informacionUsuario.value?.dataUser?.tipoPerfil == Perfiles.ADMIN.num) {
                        _isLogged.value = Perfiles.ADMIN.num
                    }

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
                    if (_isLogged.value == -1){
                        eventosViewModel.setState(EventosUIState.Error(R.string.usuarios))
                    }
                }else{
                    eventosViewModel.setState(EventosUIState.Error(R.string.conexionLogin))
                }
            } catch (e: Exception) {
                eventosViewModel.setState(EventosUIState.Error(-1))
                viewModelScope.launch {
                    val err = ErrorLog(::hacerLogin.name, "App", "$e", "", _informacionUsuario.value?.dataUser?.idUsuario ?: 0, BuildConfig.VERSION_CODE, usuarioLogin.toString(), LocalDate.now().toString())
                    if(isInternetAvailable(context))
                        repositorio.mandarError(err)
                }
            }
        }
    }
}