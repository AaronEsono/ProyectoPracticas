package com.example.practicaaaron.pantallas

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Call
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material.icons.rounded.FolderCopy
import androidx.compose.material.icons.rounded.GppBad
import androidx.compose.material.icons.rounded.LocalShipping
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.practicaaaron.BuildConfig
import com.example.practicaaaron.R
import com.example.practicaaaron.clases.errores.ErrorLog
import com.example.practicaaaron.clases.incidencias.ColoresIncidencias
import com.example.practicaaaron.clases.pedidos.PedidoCab
import com.example.practicaaaron.clases.utilidades.AnimatedPreloader
import com.example.practicaaaron.ui.viewModel.OpcionesViewModel
import com.example.practicaaaron.ui.theme.colorBarraEncima
import com.example.practicaaaron.ui.theme.colorPrimario
import java.time.LocalDate
import java.util.Base64

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "SuspiciousIndentation",
    "StateFlowValueCalledInComposition"
)
@Composable
fun VentanaPedidos(
    navHostController: NavHostController,
    opcionesViewModel: OpcionesViewModel
) {

    //Variable que guarda los distintos pedidos de cada usuario
    val pedidos = opcionesViewModel.pedidosRepartidorCopy.collectAsState()
    val esAdmin = opcionesViewModel.isLogged.collectAsState().value
    val info = opcionesViewModel.informacion.collectAsState().value
    val done = opcionesViewModel.done.collectAsState().value

    LaunchedEffect(true) {
        opcionesViewModel.setDone()

        if (esAdmin == 1)
            opcionesViewModel.obtenerPedidos()
        else
            opcionesViewModel.obtenerPedidos(opcionesViewModel.idUsuarioAdmin.value)
    }

    val state = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(0.dp, 60.dp, 0.dp, 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .background(
                    colorBarraEncima
                ),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            FilaInformacion(
                texto = stringResource(id = R.string.pedidos) + " ${info.pedidos}",
                Icons.Rounded.FolderCopy
            )
            FilaInformacion(
                texto = stringResource(id = R.string.porEntregar) + " ${info.porEntregar}",
                Icons.Rounded.LocalShipping
            )
            FilaInformacion(
                texto = stringResource(id = R.string.incidenciasPed) + " ${info.incidencia}",
                Icons.Rounded.GppBad
            )
            FilaInformacion(
                texto = stringResource(id = R.string.entregadosPed) + " ${info.entregados}",
                Icons.Rounded.LocationOn
            )
        }

        Divider(color = Color.White)

        Row (modifier = Modifier
            .fillMaxWidth()
            .height(intrinsicSize = IntrinsicSize.Max)
            .background(colorBarraEncima),
            horizontalArrangement = Arrangement.Center){
            Text(text = stringResource(id = R.string.fecha) + " ${opcionesViewModel.fecha.value.dayOfMonth}-${opcionesViewModel.fecha.value.monthValue}-${opcionesViewModel.fecha.value.year}",modifier = Modifier.padding(5.dp), color = Color.White)
        }

        Divider(color = Color.White)

        Row (modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .background(colorBarraEncima)
            .padding(5.dp, 0.dp), verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly){
            Icon(Icons.Rounded.Search, contentDescription = "Buscar", tint = Color.White, modifier = Modifier.size(25.dp))

            var buscador by remember { mutableStateOf("") }

            TextField(
                value = buscador,
                onValueChange = { buscador = it
                                opcionesViewModel.setTexto(it)},
                singleLine = true,
                label = { Text(stringResource(id = R.string.buscar)) }
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(state),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (!done) {
                AnimatedPreloader(
                    Modifier.size(100.dp),
                    animacioncompletado = R.raw.animacioncargando
                )
            } else {
                //Si no hay pedidos no mostramos nada, si hay pedidos mostrarlos en formato carta
                if (done) {
                    pedidos.value!!.data.pedidos.forEach {
                        Spacer(modifier = Modifier.padding(0.dp, 5.dp))
                        Carta(navHostController, it, opcionesViewModel)
                        Divider(thickness = 3.dp, color = colorPrimario)
                    }
                } else {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = stringResource(id = R.string.noPedidos), fontSize = 35.sp)
                    }

                }
            }

        }
    }
}

@Composable
fun FilaInformacion(texto:String, icono: ImageVector){
    Column (modifier = Modifier.fillMaxHeight(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center){
        Icon(icono, contentDescription = "", tint = Color.White, modifier = Modifier.size(25.dp))
        Text(text = texto, fontSize = 11.sp, color = Color.White)
    }
}

// Funcion que que convierte un string en base64 a bitmap
@RequiresApi(Build.VERSION_CODES.O)
fun loadImageFromBase64(texto: String,opcionesViewModel: OpcionesViewModel): ImageBitmap? {
    return try {
        val decodebytes = Base64.getDecoder().decode(texto)
        val bitmap = BitmapFactory.decodeByteArray(decodebytes, 0, decodebytes.size)
        bitmap.asImageBitmap()
    } catch (e: Exception) {
        val err = ErrorLog(::loadImageFromBase64.name,"App","$e","",opcionesViewModel.informacionUsuario.value?.dataUser?.idUsuario ?: 0, BuildConfig.VERSION_CODE,texto,LocalDate.now().toString())
        opcionesViewModel.lanzarError(err)
        null
    }
}

//Funcion que muestra en formato carta cada uno de los pedidos
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Carta(
    navHostController: NavHostController,
    pedidoCab: PedidoCab = PedidoCab(),
    opcionesViewModel: OpcionesViewModel
) {
    //Imagen que tenemos que convertir a bitmap para mostrarla
    var imagen:ImageBitmap? = ImageBitmap(1,1)

    if(pedidoCab.imagenDescripcion != ""){
        imagen = loadImageFromBase64(pedidoCab.imagenDescripcion,opcionesViewModel)
    }

    //Poner el color segun el estado
    val estado: ColoresIncidencias = opcionesViewModel.indicarColorPedido(pedidoCab.incidencia)

    Column(modifier = Modifier.fillMaxSize()) {
        Row {
            Column(modifier = Modifier.clickable {
                opcionesViewModel.obtenerPedido(pedidoCab)
                navHostController.navigate("infoPedido")
            }) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(0.75f)
                            .padding(10.dp, 0.dp, 0.dp, 0.dp)
                    ) {
                        Text(
                            text = pedidoCab.nombre,
                            fontSize = 25.sp,
                            fontWeight = FontWeight.Black
                        )

                        Row(modifier = Modifier.padding(0.dp, 5.dp)) {
                            Icon(Icons.Rounded.LocationOn, contentDescription = "Icono destino")
                            Text(
                                text = "${pedidoCab.cliente.direccion.poblacion}, ${pedidoCab.cliente.direccion.municipio}",
                                fontWeight = FontWeight.Medium,
                                fontSize = 18.sp
                            )
                        }
                        Text(
                            text = "${pedidoCab.cliente.direccion.tipoCalle} ${pedidoCab.cliente.direccion.nombreCalle}, ${pedidoCab.cliente.direccion.portal} ${pedidoCab.cliente.direccion.numero}",
                            fontWeight = FontWeight.Medium,
                            fontSize = 18.sp,
                            modifier = Modifier.padding(0.dp, 5.dp)
                        )

                    }
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(0.dp, 0.dp, 15.dp, 5.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        if (imagen != null) {
                            Image(
                                bitmap = imagen,
                                contentDescription = "Imagen producto",
                                modifier = Modifier
                                    .size(70.dp)
                                    .border(BorderStroke(2.dp, colorResource(id = R.color.black))),
                                contentScale = ContentScale.FillHeight
                            )
                        }
                    }
                }
                Row(
                    horizontalArrangement = Arrangement.Absolute.SpaceBetween, modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp, 2.dp, 10.dp, 0.dp)
                ) {
                    Row {
                        Icon(Icons.Rounded.Call, contentDescription = "Icono telefono")
                        Spacer(modifier = Modifier.padding(5.dp, 0.dp))
                        Text(
                            text = pedidoCab.cliente.telefono,
                            fontWeight = FontWeight.Medium,
                            fontSize = 18.sp
                        )
                    }
                    Row {
                        Icon(Icons.Rounded.DateRange, contentDescription = "Icono telefono")
                        Spacer(modifier = Modifier.padding(5.dp, 0.dp))
                        Text(
                            text = "${pedidoCab.fechaEntrega.dayOfMonth}-${pedidoCab.fechaEntrega.monthValue}-${pedidoCab.fechaEntrega.year}",
                            fontWeight = FontWeight.Medium,
                            fontSize = 16.sp
                        )
                    }
                }
                Divider(
                    thickness = 1.dp,
                    color = Color.Gray,
                    modifier = Modifier.padding(0.dp, 10.dp, 0.dp, 0.dp)
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp, 5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val color = if(pedidoCab.incidencia != 100) Color.Black else Color.Green
                    Icon(
                        estado.imagen,
                        contentDescription = "",
                        modifier = Modifier.size(30.dp),
                        tint = color
                    )
                    Text(
                        text = stringResource(id = estado.texto),
                        modifier = Modifier.padding(20.dp, 0.dp),
                        fontSize = 20.sp
                    )
                }
            }
        }
    }
}