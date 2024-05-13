package com.example.practicaaaron.clases.usuarios

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "usuario")
data class Usuario(
    @PrimaryKey
    val idUsuario:Int = 0,
    val username:String = "",
    val idPerfil:Int = 0,
    val tipoPerfil:Int = 0,
    val nombre:String = "",
    val email:String = ""
)