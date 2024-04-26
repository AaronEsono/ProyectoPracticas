package com.example.practicaaaron.clases.resultados

import com.google.gson.annotations.SerializedName

data class Porcentajes(
    @SerializedName("PENTREGRADOS")
    var pEntregados:Int = 0,
    @SerializedName("PINCIDENCIAS")
    var pIncidencias:Int = 0,
    @SerializedName("PSINENTREGAR")
    var pSinEntregar:Int = 0
)