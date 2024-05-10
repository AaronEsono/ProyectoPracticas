package com.example.practicaaaron.clases.pedidos

import com.google.gson.annotations.SerializedName

data class Cliente(
    @SerializedName("ID_CLIENTE")
    val idCliente:Int = 1,
    @SerializedName("DNI")
    val dni:String = "",
    @SerializedName("TELEFONO")
    val telefono:String = "",
    @SerializedName("NOMBRE")
    val nombre:String = "",
    @SerializedName("DIRECCION")
    val direccion:Direccion = Direccion()
)