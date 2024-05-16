package com.example.practicaaaron.clases.entidades.pedidos

import androidx.room.PrimaryKey

data class Cliente(
    @PrimaryKey
    var idCliente:Int,
    var dni:String,
    var telefono:String,
    var nombre:String,
    var idPedido:Int
)