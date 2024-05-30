package com.example.practicaaaron.pantallas

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.animateContentSize
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
import androidx.compose.material.icons.rounded.ArrowCircleDown
import androidx.compose.material.icons.rounded.ArrowCircleUp
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.practicaaaron.R
import com.example.practicaaaron.clases.entidades.pedidos.PedidoEntero
import com.example.practicaaaron.clases.utilidades.coloresIncidencias
import com.example.practicaaaron.ui.theme.colorBarraEncima
import com.example.practicaaaron.ui.theme.colorPrimario
import com.example.practicaaaron.ui.viewModel.PedidosViewModel
import com.example.practicaaaron.ui.viewModel.loadImageFromBase64
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "SuspiciousIndentation",
    "StateFlowValueCalledInComposition"
)
@Composable
fun VentanaPedidos(
    navHostController: NavHostController,
    pedidosViewModel: PedidosViewModel = hiltViewModel(),
    fecha: LocalDate,
    id: Int
) {

    //Variable que guarda los distintos pedidos de cada usuario

    val prueba = pedidosViewModel.pedidos.collectAsState().value
    val info = pedidosViewModel.informacion.collectAsState().value

    val context = LocalContext.current

    LaunchedEffect(true) {
        pedidosViewModel.obtenerPedidos(id, fecha, context)
    }

    val state = rememberScrollState()
    val mostrarInformacion = remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(0.dp, 60.dp, 0.dp, 0.dp)
    ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .animateContentSize()
                    .height(if (mostrarInformacion.value) 80.dp else 40.dp)
                    .background(
                        colorBarraEncima
                    ),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if(mostrarInformacion.value){
                    FilaInformacion(
                        texto = stringResource(id = R.string.pedidos) + "${info.pedidos}",
                        Icons.Rounded.FolderCopy
                    )
                    FilaInformacion(
                        texto = stringResource(id = R.string.porEntregar) + "${info.porEntregar}",
                        Icons.Rounded.LocalShipping
                    )
                    FilaInformacion(
                        texto = stringResource(id = R.string.incidenciasPed) + "${info.incidencia}",
                        Icons.Rounded.GppBad
                    )
                    FilaInformacion(
                        texto = stringResource(id = R.string.entregadosPed) + "${info.entregados}",
                        Icons.Rounded.LocationOn
                    )
                }else{
                    Icon(
                        Icons.Rounded.ArrowCircleDown,
                        contentDescription = "",
                        tint = Color.White,
                        modifier = Modifier
                            .size(25.dp)
                            .clickable { mostrarInformacion.value = true }
                    )
                }
            }

            if(mostrarInformacion.value){
                Divider(color = Color.White)

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(intrinsicSize = IntrinsicSize.Max)
                        .background(colorBarraEncima),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = stringResource(id = R.string.fecha) + " ${fecha.dayOfMonth}-${fecha.monthValue}-${fecha.year}",
                        modifier = Modifier.padding(5.dp),
                        color = Color.White
                    )
                }

                Divider(color = Color.White)

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(70.dp)
                        .background(colorBarraEncima)
                        .padding(5.dp, 0.dp), verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Icon(
                        Icons.Rounded.Search,
                        contentDescription = "Buscar",
                        tint = Color.White,
                        modifier = Modifier.size(25.dp)
                    )

                    var buscador by remember { mutableStateOf("") }

                    TextField(
                        value = buscador,
                        onValueChange = {
                            buscador = it
                            pedidosViewModel.filtrar(it, id, fecha)
                        },
                        singleLine = true,
                        label = { Text(stringResource(id = R.string.buscar)) }
                    )

                    Icon(
                        Icons.Rounded.ArrowCircleUp,
                        contentDescription = "",
                        tint = Color.White,
                        modifier = Modifier
                            .size(25.dp)
                            .clickable { mostrarInformacion.value = false }
                    )
                }
            }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(state),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (prueba.isNotEmpty()) {
                prueba.forEach {
                    Spacer(modifier = Modifier.padding(0.dp, 5.dp))
                    Carta(navHostController, it)
                    Divider(thickness = 3.dp, color = colorPrimario)
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

//Funcion que muestra en formato carta cada uno de los pedidos
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Carta(
    navHostController: NavHostController,
    pedido: PedidoEntero
) {
    //Imagen que tenemos que convertir a bitmap para mostrarla
    var imagen:ImageBitmap? = ImageBitmap(1,1)

    if(pedido.pedido.imagen != ""){
        imagen = loadImageFromBase64(pedido.pedido.imagen)
    }

    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")
    val fecha = LocalDate.parse(pedido.pedido.fEntrega,formatter)

    Column(modifier = Modifier.fillMaxSize()) {
        Row {
            Column(modifier = Modifier.clickable {
                navHostController.navigate("infoPedido/${pedido.pedido.idPedido}/$fecha")
            }) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(0.75f)
                            .padding(10.dp, 0.dp, 0.dp, 0.dp)
                    ) {
                        Text(
                            text = pedido.pedido.nombre,
                            fontSize = 25.sp,
                            fontWeight = FontWeight.Black
                        )

                        Row(modifier = Modifier.padding(0.dp, 5.dp)) {
                            Icon(Icons.Rounded.LocationOn, contentDescription = "Icono destino")
                            Text(
                                text = "${pedido.direccion.poblacion}, ${pedido.direccion.municipio}",
                                fontWeight = FontWeight.Medium,
                                fontSize = 18.sp
                            )
                        }
                        Text(
                            text = "${pedido.direccion.tipoCalle} ${pedido.direccion.nombreCalle}, ${pedido.direccion.portal} ${pedido.direccion.numero}",
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
                            text = pedido.cliente.telefono,
                            fontWeight = FontWeight.Medium,
                            fontSize = 18.sp
                        )
                    }
                    Row {
                        Icon(Icons.Rounded.DateRange, contentDescription = "Icono telefono")
                        Spacer(modifier = Modifier.padding(5.dp, 0.dp))
                        Text(
                            text = "${fecha.dayOfMonth}-${fecha.monthValue}-${fecha.year}",
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
                        .background(
                            coloresIncidencias
                                .stream()
                                .filter { it.incidencia == pedido.pedido.incidencia }
                                .findFirst()
                                .map { it.colorFondo }
                                .get()
                        ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(modifier = Modifier.padding(5.dp,0.dp))
                    Icon(
                        coloresIncidencias.stream().filter { it.incidencia == pedido.pedido.incidencia }.findFirst().map { it.imagen }.get(),
                        contentDescription = "",
                        modifier = Modifier.size(30.dp),
                        tint = coloresIncidencias.stream().filter { it.incidencia == pedido.pedido.incidencia }.findFirst().map { it.colorIcono }.get()
                    )
                    Text(
                        text = stringResource(id = coloresIncidencias.stream().filter { it.incidencia == pedido.pedido.incidencia }.findFirst().map { it.texto }.get()),
                        modifier = Modifier.padding(20.dp, 5.dp),
                        fontSize = 20.sp
                    )
                }
            }
        }
    }
}