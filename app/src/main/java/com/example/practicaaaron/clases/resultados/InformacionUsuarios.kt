package com.example.practicaaaron.clases.resultados

import com.google.gson.annotations.SerializedName

data class InformacionUsuarios (
    @SerializedName("INFORMACION")
    var resultados:Informacion = Informacion(),
    @SerializedName("PEDIDOSTOTALES")
    var pedidosTotales:Int = -1,
    @SerializedName("PORCENTAJES")
    var porcentajes:Porcentajes = Porcentajes()
)