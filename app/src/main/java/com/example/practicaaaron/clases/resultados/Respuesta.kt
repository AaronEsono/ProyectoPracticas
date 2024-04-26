package com.example.practicaaaron.clases.resultados

import com.google.gson.annotations.SerializedName

data class Respuesta (
    @SerializedName("resultados")
    var nombre:InformacionUsuarios = InformacionUsuarios(),
    @SerializedName("retcode")
    var retcode:Int = 0,
    @SerializedName("mensaje")
    var mensaje:String = ""
)