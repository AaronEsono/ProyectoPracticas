package com.example.practicaaaron.pantallas

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.practicaaaron.R
import com.example.practicaaaron.clases.usuarios.Opcion
import com.example.practicaaaron.ui.ViewModel.OpcionesViewModel
import com.example.practicaaaron.ui.theme.colorPrimario
import com.example.practicaaaron.ui.theme.colorTerciario

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "SuspiciousIndentation")
@Composable
@Preview
fun VentanaPrincipal(
    navHostController: NavHostController? = null,
    opcionesViewModel: OpcionesViewModel? = null
){
    // Distintas funciones que tiene el usuario para elegir
    var opciones = listOf(
        Opcion(R.drawable.iconopedidos,"Pedidos","pedidos","pedidos"),
        Opcion(R.drawable.iconorutas, "Rutas", "ruta", "rutas"),
        Opcion(R.drawable.iconologistica, "Logística", "logística", "pedidos"),
    )

        Column(
                modifier = Modifier
                    .padding(0.dp, 50.dp,0.dp,0.dp)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.padding(0.dp,15.dp))

                //Layout para mostrar las diferentes opciones
                LazyColumn(modifier = Modifier.padding(20.dp,0.dp)) {

                    opciones.forEach(){
                        item {
                            cartaMenuPr(
                                navHostController,
                                imagen = it.idImagen,
                                nombre = it.nombre,
                                descripcion = it.descripcionImagen,
                                ruta = it.ruta
                            )
                            Spacer(modifier = Modifier.padding(0.dp,10.dp))
                        }
                    }
                }
            }
        }

//Funcion composable que muestra en formato carta cada opcion del menu
@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun cartaMenuPr(navHostController: NavHostController? = null,
                imagen:Int = R.drawable.iconopedidos,
                nombre:String = "Rutas",
                descripcion:String = "Descripcion rutas",
                ruta:String = "") {
    val listColors = listOf(Color.Transparent, Color.Black)

    Card(
        modifier = Modifier
            .height(170.dp)
            .fillMaxWidth(),onClick = {navHostController?.navigate("$ruta")}
    ) {
        Column (modifier = Modifier
            .background(colorPrimario)
            .fillMaxSize()
            .padding(0.dp, 10.dp)){
            Column (horizontalAlignment = Alignment.Start, verticalArrangement = Arrangement.Bottom, modifier = Modifier
                .fillMaxSize()
                .padding()){
                Column (horizontalAlignment = Alignment.End, modifier = Modifier
                    .fillMaxWidth()
                    .padding(30.dp, 0.dp)){
                    Text(text = "$nombre", color = Color.White, fontSize = 30.sp)
                }
                Image(painter = painterResource(id = imagen), contentDescription = "$descripcion", modifier = Modifier.size(200.dp))
            }
        }
    }
}