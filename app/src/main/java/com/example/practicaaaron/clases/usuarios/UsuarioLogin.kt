package com.example.practicaaaron.clases.usuarios

import com.google.gson.annotations.SerializedName

data class UsuarioLogin (
    @SerializedName("username")
    var username:String = "",
    @SerializedName("password")
    var password:String = "",
)