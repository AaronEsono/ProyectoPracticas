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
import com.example.practicaaaron.ui.theme.ausente
import com.example.practicaaaron.ui.theme.ausenteIcono
import com.example.practicaaaron.ui.theme.completado
import com.example.practicaaaron.ui.theme.completadoIcono
import com.example.practicaaaron.ui.theme.direccionErronea
import com.example.practicaaaron.ui.theme.direccionErroneaIcono
import com.example.practicaaaron.ui.theme.perdido
import com.example.practicaaaron.ui.theme.perdidoIcono
import com.example.practicaaaron.ui.theme.rechazo
import com.example.practicaaaron.ui.theme.rechazoIcono

val coloresIncidencias = listOf(
    ColoresIncidencias(Icons.Rounded.Commute, 0, R.string.porEntregarPed, "Normal"),
    ColoresIncidencias(Icons.Rounded.CheckCircleOutline, 100, R.string.entregado, "Entregado",0f,completado,completadoIcono),
    ColoresIncidencias(Icons.Rounded.Block, 10, R.string.perdido, "Pérdida",0f, perdido, perdidoIcono),
    ColoresIncidencias(Icons.Rounded.Clear, 30, R.string.rechazo, "Rechazo",0f, rechazo, rechazoIcono),
    ColoresIncidencias(Icons.Rounded.AcUnit, 20, R.string.ausente, "Ausente",0f, ausente,ausenteIcono),
    ColoresIncidencias(Icons.Rounded.AccountCircle,40, R.string.direccionErronea, "dirección errónea",0f,direccionErronea,direccionErroneaIcono)
)

var listadoTraspasos:MutableList<Int> = mutableListOf()