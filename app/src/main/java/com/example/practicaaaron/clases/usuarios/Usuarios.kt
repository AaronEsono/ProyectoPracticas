package com.example.practicaaaron.clases.usuarios

import com.google.gson.annotations.SerializedName

data class Usuarios(
    @SerializedName("usuarios")
    var usuarios:List<DataUser> = listOf(),
    @SerializedName("mensaje")
    var mensaje:String = "",
    @SerializedName("retcode")
    var retcode:Int = 0,
    @SerializedName("jsonOut")
    var jsonOut:String = ""
)