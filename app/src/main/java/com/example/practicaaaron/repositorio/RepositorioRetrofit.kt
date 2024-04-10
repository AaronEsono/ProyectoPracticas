package com.example.practicaaaron.repositorio

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.practicaaaron.apiServicio.ApiServicio
import com.example.practicaaaron.apiServicio.getRetrofitClient
import com.example.practicaaaron.clases.pedidos.DataPedido
import com.example.practicaaaron.clases.usuarios.Data
import com.example.practicaaaron.clases.usuarios.UsuarioLogin
import retrofit2.Retrofit

@RequiresApi(Build.VERSION_CODES.O)
class RepositorioRetrofit(
    private val apiServicio: Retrofit = getRetrofitClient()
) {
    suspend fun recuperarPedidos(idUsuario: Int):DataPedido?{
        return apiServicio.create(ApiServicio::class.java).obtenerPedidos(idUsuario).body()
    }

    suspend fun hacerLogin(usuarioLogin: UsuarioLogin):Data?{
        return apiServicio.create(ApiServicio::class.java).hacerLogin(usuarioLogin).body()
    }

}