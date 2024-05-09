package com.example.practicaaaron.clases.pedidos

import com.google.gson.annotations.SerializedName

data class PedidoActualizar(
    @SerializedName("ID_PEDIDO")
    var idPedido:Int,
    @SerializedName("INCIDENCIA")
    var incidencia:Int,
    @SerializedName("ID_USUARIO")
    var idUsuario:Int
)