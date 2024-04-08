package com.example.practicaaaron.pantallas

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
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
import com.example.practicaaaron.objetos.Opcion
import com.example.practicaaaron.ui.ViewModel.OpcionesViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
@Preview
fun VentanaPrincipal(
    navHostController: NavHostController? = null,
    opcionesViewModel: OpcionesViewModel? = null
){
        Column(
                modifier = Modifier
                    .padding(0.dp, 60.dp)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.padding(0.dp,15.dp))

                LazyVerticalStaggeredGrid(columns = StaggeredGridCells.Adaptive(minSize = 130.dp)
                , verticalItemSpacing = 13.dp,
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.padding(20.dp,0.dp)) {

                    opcionesViewModel?.opciones?.forEach(){
                        item {
                            cartaMenuPr(
                                navHostController,
                                imagen = it.idImagen,
                                nombre = it.nombre,
                                descripcion = it.descripcionImagen
                            )
                        }
                    }
                }
            }
        }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
//Pasar parametros del obteto a la carta
fun cartaMenuPr(navHostController: NavHostController? = null,
                imagen:Int,
                nombre:String,
                descripcion:String) {
    val listColors = listOf(Color.Transparent, Color.Black)

    Card(
        modifier = Modifier.size(170.dp,170.dp),onClick = {navHostController?.navigate("pedidos")}
    ) {
        Box (){
            Image(painter = painterResource(id = imagen), contentDescription = "$descripcion", contentScale = ContentScale.FillHeight)

            Box(modifier = Modifier
                .fillMaxSize()
                .background(Brush.verticalGradient(listColors, startY = 125f)))

            Box (contentAlignment = Alignment.BottomCenter, modifier = Modifier
                .fillMaxSize()
                .padding(6.dp)){
                Text(text = "$nombre", color = Color.White, fontSize = 25.sp)
            }
        }
    }
}