package com.example.practicaaaron.pantallas

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.BusinessCenter
import androidx.compose.material.icons.rounded.Call
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material.icons.rounded.FolderCopy
import androidx.compose.material.icons.rounded.GppBad
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.LocalShipping
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.practicaaaron.R
import com.example.practicaaaron.clases.incidencias.ColoresIncidencias
import com.example.practicaaaron.clases.pedidos.PedidoCab
import com.example.practicaaaron.clases.pedidos.Pedidos
import com.example.practicaaaron.clases.utilidades.AnimatedPreloader
import com.example.practicaaaron.ui.ViewModel.OpcionesViewModel
import com.example.practicaaaron.ui.theme.colorBarraEncima
import com.example.practicaaaron.ui.theme.colorPrimario
import java.util.Base64

@OptIn(ExperimentalFoundationApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "SuspiciousIndentation",
    "StateFlowValueCalledInComposition"
)
@Composable
fun ventanaPedidos2(
    navHostController: NavHostController,
    opcionesViewModel: OpcionesViewModel
) {

    //Variable que guarda los distintos pedidos de cada usuario
    var pedidos = opcionesViewModel.pedidosRepartidor.value
    var esAdmin = opcionesViewModel.isLogged.collectAsState().value
    var info = opcionesViewModel.informacion.collectAsState().value
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
                    animacioncompletado = R.raw.animacioncargando
                )
            }
        } else {
            //Si no hay pedidos no mostramos nada, si hay pedidos mostrarlos en formato carta
            if (pedidos?.data?.pedidos != null) {
                pedidos?.data?.let { it ->

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
                                filaInformacion(
                                    texto = "Pedidos: ${info?.pedidos}",
                                    Icons.Rounded.FolderCopy
                                )
                                filaInformacion(
                                    texto = "Por entregar: ${info?.porEntregar}",
                                    Icons.Rounded.LocalShipping
                                )
                                filaInformacion(
                                    texto = "Incidencias: ${info?.incidencia}",
                                    Icons.Rounded.GppBad
                                )
                                filaInformacion(
                                    texto = "Entregados: ${info?.entregados}",
                                    Icons.Rounded.LocationOn
                                )
                            }
                        }
                    }

                    items(it.pedidos) {
                        Spacer(modifier = Modifier.padding(0.dp, 5.dp))
                        carta(navHostController, it, opcionesViewModel)
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