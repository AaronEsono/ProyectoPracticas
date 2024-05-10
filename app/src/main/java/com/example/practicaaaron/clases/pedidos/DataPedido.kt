package com.example.practicaaaron.clases.pedidos

import com.google.gson.annotations.SerializedName

data class DataPedido(
    @SerializedName("data")
    var data:Pedidos = Pedidos()
)