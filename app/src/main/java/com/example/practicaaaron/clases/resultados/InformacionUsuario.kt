package com.example.practicaaaron.clases.resultados

import com.google.gson.annotations.SerializedName

data class InformacionUsuario (
    @SerializedName("NOMBRE")
    var nombre:String = "",
    @SerializedName("ENTREGADOS")
    var entregados:Int = 0,
    @SerializedName("INCIDENCIAS")
    var incidencias:Int = 0
)