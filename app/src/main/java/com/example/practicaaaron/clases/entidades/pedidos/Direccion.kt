package com.example.practicaaaron.clases.entidades.pedidos

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "DIRECCIONES")
data class Direccion(
    @PrimaryKey
    var idDireccion:Int = 0,
    var tipoCalle:String = "",
    var nombreCalle:String = "",
    var portal:Int = 0,
    var numero:String = "",
    var poblacion:String = "",
    var municipio:String = "",
    var cp:String = "",
    var idCliente:Int = 0
)