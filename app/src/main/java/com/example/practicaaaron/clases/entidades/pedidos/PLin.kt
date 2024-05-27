package com.example.practicaaaron.clases.entidades.pedidos

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "BULTOS")
data class PLin(
    @PrimaryKey
    var idBulto:Int = 0,
    var refBulto:String = "",
    var descripcion:String = "",
    var unidades:Int = 0,
    var idPedido:Int = 0
)