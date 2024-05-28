package com.example.practicaaaron.repositorio

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.practicaaaron.apiServicio.ApiServicio
import com.example.practicaaaron.apiServicio.getRetrofitClient
import com.example.practicaaaron.clases.entrega.Entrega
import com.example.practicaaaron.clases.errores.ErrorLog
import com.example.practicaaaron.clases.incidencias.Entregado
import com.example.practicaaaron.clases.pedidos.DataPedido
import com.example.practicaaaron.clases.pedidos.PedidoActualizar
import com.example.practicaaaron.clases.resultados.Respuesta
import com.example.practicaaaron.clases.entidades.TransferirPedido
import com.example.practicaaaron.clases.usuarios.Data
import com.example.practicaaaron.clases.usuarios.UsuarioLogin
import com.example.practicaaaron.clases.usuarios.Usuarios
import retrofit2.Retrofit
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

    suspend fun mandarError(errorLog: ErrorLog){
        apiServicio.create(ApiServicio::class.java).mandarError(errorLog)
    }

    suspend fun transferirPedido(transferirPedido: TransferirPedido){
        apiServicio.create(ApiServicio::class.java).transferirPedido(transferirPedido)
    }

}