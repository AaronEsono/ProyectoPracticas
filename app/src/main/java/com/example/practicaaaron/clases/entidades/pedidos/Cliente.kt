package com.example.practicaaaron.clases.entidades.pedidos

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "CLIENTES")
data class Cliente(
    @PrimaryKey
    var idCliente:Int = 0,
    var dni:String = "",
    var telefono:String = "",
    var nombre:String = "",
)