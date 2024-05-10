package com.example.practicaaaron.clases.pedidos

import com.google.gson.annotations.SerializedName


data class Direccion(
    @SerializedName("ID_DIRECCION")
    val idDireccion:Int = 1,
    @SerializedName("TIPO_CALLE")
    val tipoCalle:String = "",
    @SerializedName("NOMBRE_CALLE")
    val nombreCalle:String = "",
    @SerializedName("PORTAL")
    val portal:Int = 1,
    @SerializedName("NUMERO")
    val numero:String = "",
    @SerializedName("POBLACION")
    val poblacion:String = "",
    @SerializedName("MUNICIPIO")
    val municipio:String = "",
    @SerializedName("CP")
    val codigoPostal:String = ""
)