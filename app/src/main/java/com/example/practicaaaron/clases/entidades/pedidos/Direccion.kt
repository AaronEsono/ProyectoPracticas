package com.example.practicaaaron.clases.entidades.pedidos

import androidx.room.PrimaryKey

data class Direccion(
    @PrimaryKey
    var idDireccion:Int,
    var tipoCalle:String,
    var nombreCalle:String,
    var portal:Int,
    var numero:String,
    var poblacion:String,
    var municipio:String,
    var cp:String,
    var idCliente:Int
)