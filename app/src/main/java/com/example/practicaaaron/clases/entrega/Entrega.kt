package com.example.practicaaaron.clases.entrega

import com.google.gson.annotations.SerializedName

data class Entrega(
    @SerializedName("FOTO_ENTREGA")
    var fotoEntrega:String = "",
    @SerializedName("LATITUD")
    var latitud:Float = 0f,
    @SerializedName("ALTITUD")
    var longitud:Float = 0f,
    @SerializedName("BARCODE")
    var lecturaBarcode:String = "",
    @SerializedName("ID_PEDIDO")
    var idPedido:Int = 0
)