package com.example.practicaaaron.clases.entidades

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Estadisticas")
data class EstadisticaUsuario(
    @PrimaryKey
    var idUsuario:Int,
    var entregados:Int = 0,
    var incidencias:Int = 0,
    var sinEntregar:Int = 0,

    var pEntregados:Int = 0,
    var pIncidencias:Int = 0,
    var pSinEntregar:Int = 0,

    var totales:Int = -1
)