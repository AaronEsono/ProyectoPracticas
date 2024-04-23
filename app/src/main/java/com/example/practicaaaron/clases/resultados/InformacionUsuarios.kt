package com.example.practicaaaron.clases.resultados

import com.google.gson.annotations.SerializedName

data class InformacionUsuarios (
    @SerializedName("resultados")
    var resultados:List<InformacionUsuario> = listOf(),
    @SerializedName("retcode")
    var retcode:Int = 0,
    @SerializedName("mensaje")
    var mensaje:String = ""
)