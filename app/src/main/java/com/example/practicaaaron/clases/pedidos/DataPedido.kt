package com.example.practicaaaron.clases.pedidos

import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class DataPedido(
    @SerializedName("data")
    val data:Pedidos = Pedidos()
)