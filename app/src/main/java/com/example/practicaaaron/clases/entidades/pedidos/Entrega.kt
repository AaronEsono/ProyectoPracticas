package com.example.practicaaaron.clases.entidades.pedidos

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ENTREGAS")
data class Entrega(
    @PrimaryKey()
    var idEntrega:Int = 0,
    var fotoEntrega:String = "",
    var codigoBarras:String = "",
    var firma:String = ""
)