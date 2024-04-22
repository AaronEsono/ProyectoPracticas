package com.example.practicaaaron.clases.incidencias

import com.google.gson.annotations.SerializedName

data class Entregado (
    @SerializedName("CONSEGUIDO")
    var conseguido:Boolean = false,
    @SerializedName("mensaje")
    var mensaje:String = "",
    @SerializedName("retcode")
    var retcode:Int = -2,
    @SerializedName("jsonOut")
    var jsonOut:String = ""
){
}