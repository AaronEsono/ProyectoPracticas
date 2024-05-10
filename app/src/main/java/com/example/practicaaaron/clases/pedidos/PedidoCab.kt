package com.example.practicaaaron.clases.pedidos

import android.os.Build
import androidx.annotation.RequiresApi
import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime


@RequiresApi(Build.VERSION_CODES.O)
data class PedidoCab(
    @SerializedName("ID_PEDIDO")
    val idPedido:Int = 1,
    @SerializedName("F_ENTREGA")
    val fechaEntrega:LocalDateTime = LocalDateTime.now(),
    @SerializedName("ESTADO")
    val entregado:Boolean = true,
    @SerializedName("NOMBRE")
    val nombre:String = "Pedido 1",
    @SerializedName("INCIDENCIA")
    var incidencia:Int = 0,
    @SerializedName("LATITUD")
    var latitud:String = "",
    @SerializedName("ALTITUD")
    var altitud:String = "",
    @SerializedName("DESCRIPCION")
    val descripcion:String = "descripcion del pedido",
    @SerializedName("ID_ENTREGA")
    val idEntrega:Int = 0,
    @SerializedName("IMAGEN")
    val imagenDescripcion:String = "",
    @SerializedName("BULTOS")
    val bultos: List<PedidoLin> = listOf(),
    @SerializedName("CLIENTE")
    val cliente: Cliente = Cliente(),
)