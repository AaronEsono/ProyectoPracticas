package com.example.practicaaaron.pantallas

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.FolderCopy
import androidx.compose.material.icons.rounded.GppBad
import androidx.compose.material.icons.rounded.LocalShipping
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.practicaaaron.R
import com.example.practicaaaron.clases.utilidades.AnimatedPreloader
import com.example.practicaaaron.ui.viewModel.OpcionesViewModel
import com.example.practicaaaron.ui.theme.colorBarraEncima
import com.example.practicaaaron.ui.theme.colorPrimario

@OptIn(ExperimentalFoundationApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "SuspiciousIndentation",
    "StateFlowValueCalledInComposition"
)
@Composable
fun VentanaPedidos2(
    navHostController: NavHostController,
    opcionesViewModel: OpcionesViewModel
) {

    //Variable que guarda los distintos pedidos de cada usuario
    val pedidos = opcionesViewModel.pedidosRepartidor.value
    val esAdmin = opcionesViewModel.isLogged.collectAsState().value
    val info = opcionesViewModel.informacion.collectAsState().value
    var done = opcionesViewModel.done.collectAsState().value

    LaunchedEffect(true) {
        done = false

        if (esAdmin == 1)
            opcionesViewModel.obtenerPedidos()
        else
            opcionesViewModel.obtenerPedidos(opcionesViewModel.idUsuarioAdmin.value)
    }

    Log.i("done","$done")

    LazyColumn(
        modifier = Modifier
            .padding(0.dp, 60.dp, 0.dp, 0.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (!done) {
            item {
                AnimatedPreloader(
                    Modifier.size(100.dp),
                    animacioncompletado = R.raw.animacioncargando,
                    1.0f
                )
            }
        } else {
            //Si no hay pedidos no mostramos nada, si hay pedidos mostrarlos en formato carta
            if (pedidos?.data?.pedidos != null) {
                pedidos.data.let { it ->

                    stickyHeader {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(80.dp)
                                .background(
                                    colorBarraEncima
                                )
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                FilaInformacion(
                                    texto = "Pedidos: ${info.pedidos}",
                                    Icons.Rounded.FolderCopy
                                )
                                FilaInformacion(
                                    texto = "Por entregar: ${info.porEntregar}",
                                    Icons.Rounded.LocalShipping
                                )
                                FilaInformacion(
                                    texto = "Incidencias: ${info.incidencia}",
                                    Icons.Rounded.GppBad
                                )
                                FilaInformacion(
                                    texto = "Entregados: ${info.entregados}",
                                    Icons.Rounded.LocationOn
                                )
                            }
                        }
                    }

                    items(it.pedidos) {
                        Spacer(modifier = Modifier.padding(0.dp, 5.dp))
                        Carta(navHostController, it, opcionesViewModel)
                        Divider(thickness = 3.dp, color = colorPrimario)
                    }
                }
            } else {
                item {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "No hay pedidos", fontSize = 35.sp)
                    }
                }
            }
        }

    }
}