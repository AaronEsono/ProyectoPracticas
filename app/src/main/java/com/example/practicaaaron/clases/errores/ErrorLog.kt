package com.example.practicaaaron.clases.errores

import android.os.Build
import androidx.annotation.RequiresApi
import com.google.gson.annotations.SerializedName
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.Date

data class ErrorLog @RequiresApi(Build.VERSION_CODES.O) constructor(
    @SerializedName("PROCESO_METODO")
    val procesoMetodo: String = "",
    @SerializedName("ENTORNO")
    val entorno: String = "",
    @SerializedName("MENSAJE")
    val mensaje: String = "",
    @SerializedName("INNER_EXCEPTION")
    val innerException: String = "",
    @SerializedName("USUARIO")
    val usuario: Int = 0,
    @SerializedName("VERSION")
    val version: Int = 0,
    @SerializedName("PARAMETROS")
    val parametros: String = "",
    @SerializedName("F_CREACION")
    val fCreacion: LocalDateTime? = LocalDateTime.now()
)