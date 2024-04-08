package com.example.practicaaaron.clases

data class Cliente(
    val idCliente:Int,
    val dni:String,
    val telefono:String,
    val nombre:String,
    val direcciones:List<Direccion>
)