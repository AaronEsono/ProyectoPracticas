package com.example.practicaaaron.clases.pedidos

import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class Pedidos(
    @SerializedName("pedidos")
    var pedidos:List<PedidoCab> = listOf(),
    @SerializedName("mensaje")
    var mensaje:String = "",
    @SerializedName("retcode")
    var retcode:Int = 0,
    @SerializedName("jsonOut")
    var jsonOut:String = ""
)