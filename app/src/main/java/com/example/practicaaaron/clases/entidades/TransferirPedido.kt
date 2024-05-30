package com.example.practicaaaron.clases.entidades

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(tableName = "TRASPASOSINC")
data class TransferirPedido(
    @SerializedName("IDUSUARIOREPARTIDOR")
    var idUsuario:Int = 0,
    @PrimaryKey
    @SerializedName("IDPEDIDO")
    var idPedido:Int = 0,
    @SerializedName("IDDESTINATARIO")
    var idDestinatario:Int = 0,
    @Expose(serialize = false)
    var porEntregar:Boolean = false
)