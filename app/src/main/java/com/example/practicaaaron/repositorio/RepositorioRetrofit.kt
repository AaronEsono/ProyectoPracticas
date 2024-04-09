package com.example.practicaaaron.repositorio

import com.example.practicaaaron.apiServicio.ApiServicio
import com.example.practicaaaron.apiServicio.getRetrofitClient
import com.example.practicaaaron.clases.pedidos.DataPedido
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.awaitResponse


class RepositorioRetrofit(
    private val apiServicio: Retrofit = getRetrofitClient()
) {
    suspend fun recuperarPedidos():DataPedido?{
        return apiServicio.create(ApiServicio::class.java).obtenerPedidos(1).body()
    }
}