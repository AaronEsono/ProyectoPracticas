package com.example.practicaaaron.clases.pedidos

data class Entrega(
    val idEntrega:Int,
    val fotoEntrega:Int,
    val lecturaBarcode:String,
    val latitud:Float,
    val longitud:Float,
    val entregado:Boolean,
)