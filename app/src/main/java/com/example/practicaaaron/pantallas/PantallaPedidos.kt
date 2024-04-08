package com.example.practicaaaron.pantallas

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.practicaaaron.R
import com.example.practicaaaron.ui.ViewModel.OpcionesViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun ventanaPedidos(navHostController: NavHostController? = null) {
            LazyColumn(
                modifier = Modifier.padding(0.dp,60.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Add 5 items
                item {
                    Text(
                        text = "Pedidos de hoy",
                        modifier = Modifier.padding(0.dp, 10.dp, 10.dp, 0.dp),
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Black
                    )
                }

                items(10) { index ->
                    Spacer(modifier = Modifier.padding(0.dp, 5.dp))
                    carta(navHostController)
                    Spacer(modifier = Modifier.padding(0.dp, 5.dp))
                }
            }
        }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
//Pasar parametros del obteto a la carta
fun carta(navHostController: NavHostController? = null) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
        ),
        modifier = Modifier
            .size(width = 400.dp, height = 135.dp)
            .padding(15.dp, 0.dp), onClick = {navHostController?.navigate("infoPedido")}
    ) {
        Row {
            Image(painter = painterResource(id = R.drawable.imagen), contentDescription = "Imagen producto"
                , modifier = Modifier
                    .padding(5.dp)
                    .size(80.dp)
                    .clip(RoundedCornerShape(5.dp)))
            Column (){
                Text(text = "Nombre objeto", fontSize = 25.sp,modifier = Modifier.padding(60.dp,2.dp,0.dp,0.dp), fontWeight = FontWeight.Black)

                Row (modifier = Modifier.padding(0.dp,5.dp)){
                    Icon(Icons.Rounded.LocationOn, contentDescription = "Icono destino")
                    Text(text = "Torrejón de Ardoz, Madrid", fontWeight = FontWeight.Medium, fontSize = 18.sp)
                }
                Text(text = "Calle Sevilla 42 5ºF", fontWeight = FontWeight.Medium, fontSize = 18.sp
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
                Text(text = "123456789", fontWeight = FontWeight.Medium, fontSize = 18.sp)
            }
            Row {
                Icon(Icons.Rounded.DateRange, contentDescription = "Icono telefono")
                Spacer(modifier = Modifier.padding(5.dp,0.dp))
                Text(text = "4/04/2024", fontWeight = FontWeight.Medium, fontSize = 18.sp)
            }
        }
    }
}