package com.example.practicaaaron.clases.entidades.pedidos

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "PEDIDOS")
data class PCab(
    @PrimaryKey
    var idPedido:Int,
    var fEntrega:LocalDate,
    var estado:Int,
    var nombre:String,
    var incidencia:Int,
    var latitud:Float,
    var altitud:Float,
    var descripcion:String,
    var imagen:String,
    var idUser:Int,
    var idCliente:Int,
    var idEntrega:Int
)