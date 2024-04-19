package com.example.practicaaaron.apiServicio

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.practicaaaron.clases.entrega.Entrega
import com.example.practicaaaron.clases.pedidos.DataPedido
import com.example.practicaaaron.clases.pedidos.PedidoActualizar
import com.example.practicaaaron.clases.usuarios.Data
import com.example.practicaaaron.clases.usuarios.UsuarioLogin
import com.example.practicaaaron.clases.usuarios.Usuarios
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

interface ApiServicio {
    @GET("Pedidos/{ID_USUARIO}")
    suspend fun obtenerPedidos(@Path("ID_USUARIO") ID_USUARIO:Int): Response<DataPedido>
    @POST("Login")
    suspend fun hacerLogin(@Body user:UsuarioLogin):Response<Data>

    @PUT("PedidosActualizar")
    suspend fun actualizarPedido(@Body cuerpo:PedidoActualizar)

    @PUT("hacerEntrega")
    suspend fun hacerEntrega(@Body cuerpo:Entrega)

    @GET("obtenerTodos")
    suspend fun obtenerTodos():Usuarios

}

@RequiresApi(Build.VERSION_CODES.O)
fun getRetrofitClient(): Retrofit {

    val gson = GsonBuilder().registerTypeAdapter(
        LocalDateTime::class.java,
        JsonDeserializer { json, type, jsonDeserializationContext ->
            LocalDateTime.parse(json.asJsonPrimitive.asString,
                DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")) })
        .create()

    // Create or client
     return Retrofit.Builder()
        .baseUrl("https://integracion.icp.es/ws_practicas_app/api/") // Specify your base URL
        .addConverterFactory(GsonConverterFactory.create(gson)) // Specify JSon convertion method
        .client(OkHttpClient())// Add converter factory for Gson
        .build()

}
