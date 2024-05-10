package com.example.practicaaaron.clases.pedidos

import com.google.gson.annotations.SerializedName

data class PedidoLin(
    @SerializedName("ID_BULTO")
    val idBulto: Int = 1,
    @SerializedName("REFERENCIA")
    val refBulto:String = "",
    @SerializedName("DESCRIPCION")
    val descripcion:String = "",
    @SerializedName("UNIDADES")
    val unidades:Int = 1,
)