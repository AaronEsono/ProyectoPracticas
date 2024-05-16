package com.example.practicaaaron.clases.entidades.pedidos

import android.graphics.Bitmap
import androidx.room.PrimaryKey

data class Entrega(
    @PrimaryKey(autoGenerate = true)
    var idEntrega:Int,
    var fotoEntrega:String,
    var codigoBarras:String,
    var firma:Bitmap,
    var idPedido:Int
)