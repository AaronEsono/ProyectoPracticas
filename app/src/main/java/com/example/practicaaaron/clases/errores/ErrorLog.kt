package com.example.practicaaaron.clases.errores

import android.os.Build
import androidx.annotation.RequiresApi
import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

@RequiresApi(Build.VERSION_CODES.O)
data class ErrorLog(
    @SerializedName("PROCESO_METODO")
    var procesoMetodo: String = "",
    @SerializedName("ENTORNO")
    var entorno: String = "",
    @SerializedName("MENSAJE")
    var mensaje: String = "",
    @SerializedName("INNER_EXCEPTION")
    var innerException: String = "",
    @SerializedName("USUARIO")
    var usuario: Int = 1,
    @SerializedName("VERSION")
    var version: Int = 1,
    @SerializedName("PARAMETROS")
    var parametros: String = "",
    @SerializedName("F_CREACION")
    var fCreacion: String = LocalDateTime.now().toString()
)