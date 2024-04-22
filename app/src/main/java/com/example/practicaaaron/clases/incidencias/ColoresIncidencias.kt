package com.example.practicaaaron.clases.incidencias

import com.example.practicaaaron.R
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory

data class ColoresIncidencias (
    var imagen:Int = R.drawable.incidencias,
    var incidencia:Int = -1,
    var texto:String = "",
    var nombre:String = "",
    var colorMap:Float = 0f
)