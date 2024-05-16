package com.example.practicaaaron.clases.entidades.pedidos

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "BULTOS")
data class PLin(
    @PrimaryKey
    var idBulto:Int,
    var referencia:String,
    var descripcion:String,
    var unidades:Int,
    var idPedido:Int
)