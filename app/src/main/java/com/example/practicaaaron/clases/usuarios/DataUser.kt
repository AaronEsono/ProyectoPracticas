package com.example.practicaaaron.clases.usuarios

data class DataUser (
    val idUsuario:Int,
    val username:String,
    val idPerfil:Int,
    val nombre:String,
    val email:String,
    var Mensaje:String,
    var Retcode:Int,
    var jsonOut:String
){
}