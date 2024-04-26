package com.example.practicaaaron.clases.resultados

import com.google.gson.annotations.SerializedName

data class Informacion (
    @SerializedName("ENTREGADOS")
    var entregados:Int = 0,
    @SerializedName("INCIDENCIAS")
    var incidencias:Int = 0,
    @SerializedName("SINENTREGAR")
    var sientregar:Int = 0
)