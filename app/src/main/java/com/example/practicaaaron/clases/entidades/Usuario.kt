package com.example.practicaaaron.clases.entidades

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "USUARIO")
data class Usuario(
    @PrimaryKey
    var idUsuario:Int = 0,
    var username:String = "",
    var idPerfil:Int = 0,
    var tipoPerfil:Int = 0,
    var nombre:String = "",
    var email:String = ""
)