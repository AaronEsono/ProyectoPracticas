package com.example.practicaaaron.clases.incidencias

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.practicaaaron.R
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory

data class ColoresIncidencias (
    var imagen:ImageVector = Icons.Rounded.Close,
    var incidencia:Int = -1,
    var texto:String = "",
    var nombre:String = "",
    var colorMap:Float = 0f
)