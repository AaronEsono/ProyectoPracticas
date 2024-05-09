package com.example.practicaaaron.repositorio

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.practicaaaron.apiServicio.ApiServicio
import com.example.practicaaaron.apiServicio.getRetrofitClient
import com.example.practicaaaron.clases.entrega.Entrega
import com.example.practicaaaron.clases.errores.ErrorLog
import com.example.practicaaaron.clases.incidencias.Entregado
import com.example.practicaaaron.clases.pedidos.DataPedido
import com.example.practicaaaron.clases.pedidos.PedidoActualizar
import com.example.practicaaaron.clases.resultados.InformacionUsuarios
import com.example.practicaaaron.clases.resultados.Respuesta
import com.example.practicaaaron.clases.usuarios.Data
import com.example.practicaaaron.clases.usuarios.UsuarioLogin
import com.example.practicaaaron.clases.usuarios.Usuarios
import retrofit2.Retrofit
import retrofit2.create
import java.lang.Error
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
class RepositorioRetrofit(
    private val apiServicio: Retrofit = getRetrofitClient()
) {
    suspend fun recuperarPedidos(idUsuario: Int, fecha:LocalDate):DataPedido?{
        return apiServicio.create(ApiServicio::class.java).obtenerPedidos(idUsuario,fecha).body()
    }

    suspend fun hacerLogin(usuarioLogin: UsuarioLogin):Data?{
        return apiServicio.create(ApiServicio::class.java).hacerLogin(usuarioLogin).body()
    }

    suspend fun actualizarPedido(pedido:PedidoActualizar):Entregado{
        return apiServicio.create(ApiServicio::class.java).actualizarPedido(pedido)
    }

    suspend fun hacerEntrega(entrega: Entrega):Entregado{
        Log.i("entro","entroAqui2")
        return apiServicio.create(ApiServicio::class.java).hacerEntrega(entrega)
    }

    suspend fun obtenerTodos():Usuarios{
        return apiServicio.create(ApiServicio::class.java).obtenerTodos()
    }

    suspend fun resultadosTrabajadores(id:Int):Respuesta{
        return apiServicio.create(ApiServicio::class.java).resultadosTrabajadores(id)
    }

    suspend fun cerrarSesion(id:Int){
        return apiServicio.create(ApiServicio::class.java).cerrarSesion(id)
    }

    suspend fun mandarError(error: ErrorLog){
        Log.i("error","${error.toString()}")
        apiServicio.create(ApiServicio::class.java).mandarError(error)
    }
}