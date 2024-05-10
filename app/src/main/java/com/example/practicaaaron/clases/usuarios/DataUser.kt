package com.example.practicaaaron.clases.usuarios

import com.google.gson.annotations.SerializedName

data class DataUser (
    @SerializedName("ID_USUARIO")
    val idUsuario:Int,
    @SerializedName("USERNAME")
    val username:String,
    @SerializedName("ID_PERFIL")
    val idPerfil:Int,
    @SerializedName("TIPO_PERFIL")
    val tipoPerfil:Int = 0,
    @SerializedName("NOMBRE")
    val nombre:String,
    @SerializedName("EMAIL")
    val email:String,
    @SerializedName("mensaje")
    var mensaje:String,
    @SerializedName("retcode")
    var retcode:Int,
    @SerializedName("jsonOut")
    var jsonOut:String
)