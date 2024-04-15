package com.example.practicaaaron.pantallas

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Call
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.practicaaaron.clases.pedidos.PedidoCab
import com.example.practicaaaron.ui.ViewModel.OpcionesViewModel
import java.util.Base64

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
@Preview
fun ventanaPedidos(
    navHostController: NavHostController? = null,
    opcionesViewModel: OpcionesViewModel? = null
) {

        //Variable que guarda los distintos pedidos de cada usuario
        var pedidos = opcionesViewModel?.pedidosRepartidor?.collectAsState()?.value

        //Obtenemos los datos de los pedidos a traves del view model
        opcionesViewModel?.obtenerPedidos()

            LazyColumn(
                modifier = Modifier
                    .padding(0.dp, 60.dp, 0.dp, 0.dp)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    Text(
                        text = "Pedidos de hoy",
                        modifier = Modifier.padding(0.dp, 10.dp, 10.dp, 0.dp),
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Black
                    )
                }

                //Si no hay pedidos no mostramos nada, si hay pedidos mostrarlos en formato carta
                if(pedidos?.data?.pedidos != null){
                    pedidos?.data?.let {
                        items(it.pedidos){
                            Spacer(modifier = Modifier.padding(0.dp,5.dp))
                            carta(navHostController,it,opcionesViewModel)
                            Spacer(modifier = Modifier.padding(0.dp,5.dp))
                        }
                    }
                }
            }
        }

// Funcion que que convierte un string en base64 a bitmap
@RequiresApi(Build.VERSION_CODES.O)
fun loadImageFromBase64(context: Context,texto:String):ImageBitmap?{
    try{
        val decodebytes = Base64.getDecoder().decode(texto)
        val bitmap = BitmapFactory.decodeByteArray(decodebytes,0,decodebytes.size)
        return bitmap.asImageBitmap()
    }catch (e:Exception){
        return null
    }
}

//Funcion que muestra en formato carta cada uno de los pedidos
@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun carta(
    navHostController: NavHostController? = null,
    pedidoCab: PedidoCab,
    opcionesViewModel: OpcionesViewModel?,
    ) {

    val context = LocalContext.current
    //Imagen que tenemos que convertir a bitmap para mostrarla
    val imagen = loadImageFromBase64(context,pedidoCab.imagenDescripcion)

    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
        ),
        modifier = Modifier
            .width(400.dp)
            .height(intrinsicSize = IntrinsicSize.Max)
            .padding(15.dp, 0.dp),
            onClick = {navHostController?.navigate("infoPedido")
                opcionesViewModel?.obtenerPedido(pedidoCab)
            }
    ) {

        //Poner el color segun el estado
        var color = opcionesViewModel?.indicarColorPedido(pedidoCab.incidencia)?:0

        Column (modifier = Modifier.background(Color(color)).fillMaxSize()){
            Row {
                if (imagen != null) {
                    Image(bitmap = imagen, contentDescription = "Imagen producto", modifier = Modifier
                        .padding(5.dp, 5.dp, 0.dp, 5.dp)
                        .size(80.dp, 80.dp)
                        .height(intrinsicSize = IntrinsicSize.Max)
                        .clip(RoundedCornerShape(5.dp)), contentScale = ContentScale.FillHeight)
                }
                Column (){
                    Column (modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally){
                        Text(text = "${pedidoCab.nombre}", fontSize = 25.sp,fontWeight = FontWeight.Black)
                    }

                    Row (modifier = Modifier.padding(0.dp,5.dp)){
                        Icon(Icons.Rounded.LocationOn, contentDescription = "Icono destino")
                        Text(text = "${pedidoCab.cliente.direccion.poblacion}, ${pedidoCab.cliente.direccion.municipio}", fontWeight = FontWeight.Medium, fontSize = 18.sp)
                    }
                    Text(text = "${pedidoCab.cliente.direccion.tipoCalle} ${pedidoCab.cliente.direccion.nombreCalle}, ${pedidoCab.cliente.direccion.portal} ${pedidoCab.cliente.direccion.numero}", fontWeight = FontWeight.Medium, fontSize = 18.sp
                        , modifier = Modifier.padding(10.dp,0.dp))
                }
            }
            Divider(color = Color.Black, modifier = Modifier.padding(10.dp,3.dp), thickness = 3.dp)

            Row (horizontalArrangement = Arrangement.Absolute.SpaceAround, modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp, 2.dp)){
                Row {
                    Icon(Icons.Rounded.Call, contentDescription = "Icono telefono")
                    Spacer(modifier = Modifier.padding(5.dp,0.dp))
                    Text(text = "${pedidoCab.cliente.telefono}", fontWeight = FontWeight.Medium, fontSize = 18.sp)
                }
                Row {
                    Icon(Icons.Rounded.DateRange, contentDescription = "Icono telefono")
                    Spacer(modifier = Modifier.padding(5.dp,0.dp))
                    Text(text = "${pedidoCab.fechaEntrega.dayOfMonth}-${pedidoCab.fechaEntrega.monthValue}-${pedidoCab.fechaEntrega.year}", fontWeight = FontWeight.Medium, fontSize = 16.sp)
                }
            }
        }
    }
}