package com.example.practicaaaron.clases.entidades.pedidos

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.practicaaaron.clases.entidades.Usuario
import java.time.LocalDate

@Entity(
    tableName = "PEDIDOS"
)
data class PCab(
    @PrimaryKey
    var idPedido:Int = -1,
    var fEntrega:String = "",
    var estado:Int = 0,
    var nombre:String = "",
    var incidencia:Int = 0,
    var latitud:Float = 0f,
    var altitud:Float = 0f,
    var descripcion:String = "",
    var imagen:String = "",
    var idUsuario:Int = 0,
    var idCliente:Int = 0,
    var idEntrega:Int = 0,
    var idDireccion:Int = 0,
    var porEntregar:Boolean = false
)