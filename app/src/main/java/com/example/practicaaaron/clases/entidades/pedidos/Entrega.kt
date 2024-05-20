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
    var latitud:Float = 0f,
    var altitud:Float = 0f,
    var firma:String = ""
)