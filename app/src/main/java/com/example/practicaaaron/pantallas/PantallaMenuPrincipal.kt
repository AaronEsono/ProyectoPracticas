package com.example.practicaaaron.pantallas

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.practicaaaron.R
import com.example.practicaaaron.clases.pedidos.Perfiles
import com.example.practicaaaron.clases.usuarios.Opcion
import com.example.practicaaaron.ui.theme.colorPrimario
import com.example.practicaaaron.ui.viewModel.MenuViewModel
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "SuspiciousIndentation")
@Composable
fun VentanaPrincipal(
    navHostController: NavHostController,
    menuViewModel: MenuViewModel = hiltViewModel()
) {
    // Distintas funciones que tiene el usuario para elegir
    val opcionesUser = listOf(
        Opcion(R.drawable.iconopedidos, R.string.pedido, R.string.dPedido, "pedidos"),
        Opcion(R.drawable.iconorutas, R.string.ruta, R.string.dRuta, "rutas"),
        Opcion(R.drawable.iconologistica, R.string.logistica, R.string.dLogistica, "futuro"),
        Opcion(R.drawable.traspasos, R.string.traspasos, R.string.traspasos, "traspasos"),
    )

    val opcionesAdmin = listOf(
        Opcion(R.drawable.iconoconsultar, R.string.consultar, R.string.dConsultar, "usuarios"),
        Opcion(R.drawable.icono2, R.string.estadisticas, R.string.dEstadisticas, "estadistica")
    )

    LaunchedEffect(true) {
        menuViewModel.getDatos()
    }

    val tipoPerfil = menuViewModel.tipoPerfil.collectAsState().value
    val id = menuViewModel.idUser.collectAsState().value

    if (tipoPerfil != -1) {
        Column(
            modifier = Modifier
                .padding(0.dp, 50.dp, 0.dp, 0.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.padding(0.dp, 15.dp))
            //Layout para mostrar las diferentes opciones
            if (tipoPerfil == Perfiles.USUARIO.num)
                MostrarOpciones(opcionesUser, navHostController, id)
            else
                MostrarOpciones(opcionesAdmin, navHostController)
        }
    }
}

 @RequiresApi(Build.VERSION_CODES.O)
 @Composable
fun MostrarOpciones(
     opcionesUser: List<Opcion>,
     navHostController: NavHostController,
     id: Int = 0
 ) {
    Column(modifier = Modifier.padding(20.dp,0.dp).verticalScroll(rememberScrollState())) {
        opcionesUser.forEach{
                CartaMenuPr(
                    navHostController,
                    imagen = it.idImagen,
                    nombre = it.nombre,
                    descripcion = it.descripcionImagen,
                    ruta = it.ruta,
                    id
                )
                Spacer(modifier = Modifier.padding(0.dp,10.dp))
        }
    }
}


//Funcion composable que muestra en formato carta cada opcion del menu
@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartaMenuPr(
    navHostController: NavHostController,
    imagen: Int = R.drawable.iconopedidos,
    nombre: Int = 1,
    descripcion: Int = 1,
    ruta: String = "",
    id: Int
) {

    Card(
        modifier = Modifier
            .height(170.dp)
            .fillMaxWidth(),onClick = {
                if(ruta == "pedidos"){
                    navHostController.navigate("$ruta/${LocalDate.now()}/$id")
                }else{
                    navHostController.navigate(ruta)
                }
            }
    ) {
        Column (modifier = Modifier
            .background(colorPrimario)
            .fillMaxSize()
            .padding(0.dp, 10.dp)){
                Column (horizontalAlignment = Alignment.End, modifier = Modifier
                    .fillMaxWidth()
                    .padding(30.dp, 0.dp)){
                    Text(text = stringResource(id = nombre), color = Color.White, fontSize = 30.sp)
                }
                Image(painter = painterResource(id = imagen), contentDescription = stringResource(id = descripcion), modifier = Modifier.size(200.dp))
            }
    }
}