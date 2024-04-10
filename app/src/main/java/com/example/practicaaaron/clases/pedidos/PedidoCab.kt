package com.example.practicaaaron.clases.pedidos

import android.os.Build
import android.os.Parcelable
import androidx.annotation.RequiresApi
import com.google.gson.annotations.SerializedName
import java.time.LocalDate
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
    val nombre:String = "",
    @SerializedName("INCIDENCIA")
    val incidencia:Int = 0,
    @SerializedName("DESCRIPCION")
    val descripcion:String = "",
    @SerializedName("IMAGEN")
    val imagenDescripcion:String = "",
    @SerializedName("BULTOS")
    val bultos: List<PedidoLin> = listOf(),
    @SerializedName("CLIENTE")
    val cliente: Cliente = Cliente(),
)