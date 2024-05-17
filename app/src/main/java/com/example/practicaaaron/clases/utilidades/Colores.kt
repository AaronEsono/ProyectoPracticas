package com.example.practicaaaron.clases.utilidades

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AcUnit
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.Block
import androidx.compose.material.icons.rounded.CheckCircleOutline
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.Commute
import com.example.practicaaaron.R
import com.example.practicaaaron.clases.incidencias.ColoresIncidencias

val coloresIncidencias = listOf(
    ColoresIncidencias(Icons.Rounded.Commute, 0, R.string.porEntregarPed, "Normal"),
    ColoresIncidencias(Icons.Rounded.CheckCircleOutline, 100, R.string.entregado, "Entregado"),
    ColoresIncidencias(Icons.Rounded.Block, 10, R.string.perdido, "Pérdida"),
    ColoresIncidencias(Icons.Rounded.Clear, 30, R.string.rechazo, "Rechazo"),
    ColoresIncidencias(Icons.Rounded.AcUnit, 20, R.string.ausente, "Ausente"),
    ColoresIncidencias(Icons.Rounded.AccountCircle,40, R.string.direccionErronea, "dirección errónea")
)