package com.example.practicaaaron.clases.incidencias

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.ui.graphics.vector.ImageVector

data class ColoresIncidencias (
    var imagen:ImageVector = Icons.Rounded.Close,
    var incidencia:Int = -1,
    var texto:Int = 1,
    var nombre:String = "",
    var colorMap:Float = 0f
)