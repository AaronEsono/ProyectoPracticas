package com.example.practicaaaron.pantallas

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.animation.Crossfade
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Call
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.CheckCircleOutline
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.practicaaaron.R
import com.example.practicaaaron.clases.entidades.pedidos.PedidoEntero
import com.example.practicaaaron.clases.utilidades.coloresIncidencias
import com.example.practicaaaron.ui.theme.seleccionado
import com.example.practicaaaron.ui.viewModel.TraspasosViewModel
import com.example.practicaaaron.ui.viewModel.loadImageFromBase64
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PantallaTraspasos(traspasosViewModel: TraspasosViewModel = hiltViewModel()){

    val pedidos = traspasosViewModel.pedidos.collectAsState().value
    val context = LocalContext.current

    Log.i("pedidos","$pedidos")

    LaunchedEffect (true){
        traspasosViewModel.getPedidosTraspasos(context)
    }
    
    Column (modifier = Modifier
        .fillMaxSize()
        .padding(0.dp, 60.dp, 0.dp, 0.dp)
        .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally){

        Spacer(modifier = Modifier.padding(0.dp,6.dp))
        pedidos.forEach { 
            CartaTraspaso(pedido = it)
        }
    }

}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CartaTraspaso(
    pedido: PedidoEntero
) {

    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")
    val fecha = LocalDate.parse(pedido.pedido.fEntrega,formatter)

    val seleccion = remember { mutableStateOf(false) }
    val color = remember { mutableStateOf(Color.Transparent) }

    Row(modifier = Modifier
        .fillMaxSize()
        .padding(15.dp, 6.dp)
        .background(
            color.value, shape = RoundedCornerShape(15.dp)
        )
        .border(border = BorderStroke(width = 1.dp, Color.LightGray), shape = RoundedCornerShape(15.dp))
        .clickable {
            seleccion.value = !seleccion.value
        }) {

        Crossfade(targetState = seleccion.value, label = "") { it ->
            if (it) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(0.2f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        Icons.Rounded.CheckCircle,
                        contentDescription = "",
                        modifier = Modifier.size(75.dp)
                    )
                }
                color.value = seleccionado
            } else {
                color.value = Color.Transparent
            }
        }
        Column (modifier = Modifier
            .fillMaxSize()
            .padding(15.dp, 10.dp, 5.dp, 10.dp)){
            Row (modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween){
                Text(text = pedido.pedido.nombre, fontSize = 20.sp, fontWeight = FontWeight.Black)
                Text(text = "${fecha.dayOfMonth}-${fecha.monthValue}-${fecha.year}", modifier = Modifier.padding(15.dp,0.dp))
            }
            Row (modifier = Modifier.fillMaxWidth()){
                Icon(Icons.Rounded.LocationOn, contentDescription = "Icono destino")
                Text(
                    text = "${pedido.direccion.poblacion}, ${pedido.direccion.municipio}",
                    fontWeight = FontWeight.Medium,
                    fontSize = 15.sp
                )
            }

            Row (modifier = Modifier.fillMaxWidth()){
                Text(
                    text = "${pedido.direccion.tipoCalle} ${pedido.direccion.nombreCalle}, ${pedido.direccion.portal} ${pedido.direccion.numero}",
                    fontWeight = FontWeight.Medium,
                    fontSize = 15.sp,
                    modifier = Modifier.padding(0.dp, 5.dp)
                )
            }

            Row (modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically){
                Icon(
                    coloresIncidencias.stream().filter { it.incidencia == pedido.pedido.incidencia }.findFirst().map { it.imagen }.get(),
                    contentDescription = "",
                    modifier = Modifier.size(30.dp)
                )
                Text(
                    text = stringResource(id = coloresIncidencias.stream().filter { it.incidencia == pedido.pedido.incidencia }.findFirst().map { it.texto }.get()),
                    fontSize = 15.sp
                )
            }
        }

    }
    
}