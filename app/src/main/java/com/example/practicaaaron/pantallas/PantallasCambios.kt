package com.example.practicaaaron.pantallas

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.practicaaaron.clases.usuarios.DataUser
import com.example.practicaaaron.ui.theme.seleccionado
import com.example.practicaaaron.ui.viewModel.PantallasCambioViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PantallaCambio(pantallasCambioViewModel: PantallasCambioViewModel = hiltViewModel()){

    val usuarios = pantallasCambioViewModel.usuarios.collectAsState().value
    val seleccionar = remember{ mutableIntStateOf(-1) }

    val contexto = LocalContext.current

    LaunchedEffect (true){
        pantallasCambioViewModel.conseguirPersonas(contexto)
    }

    Column (modifier = Modifier
        .padding(0.dp, 65.dp, 0.dp, 0.dp)
        .fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally){
        usuarios.forEach {
            CartaUsuario(it,seleccionar)
        }
        if(seleccionar.intValue != -1){
            Button(onClick = {pantallasCambioViewModel.hacerIntercambio(seleccionar.intValue,contexto)}) {
                Text(text = "Aceptar")
            }
        }
    }
}

@Composable
fun CartaUsuario(dataUser: DataUser,seleccionar:MutableState<Int>){
    val seleccion = remember { mutableStateOf(false) }

    val color = if(seleccionar.value == dataUser.idUsuario) seleccionado else Color.Transparent

    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(15.dp, 6.dp)
        .border(
            border = BorderStroke(width = 1.dp, Color.LightGray),
            shape = RoundedCornerShape(15.dp)
        ).background(color,shape = RoundedCornerShape(15.dp))
        .clickable { seleccionar.value = dataUser.idUsuario
                    seleccion.value = !seleccion.value}) {

        Column (modifier = Modifier
            .padding(15.dp, 10.dp, 5.dp, 10.dp)){
            Row (modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center){
                Text(text = dataUser.username, fontSize = 20.sp, fontWeight = FontWeight.Black)
            }
            Spacer(modifier = Modifier.padding(0.dp,5.dp))
            Row (modifier = Modifier.fillMaxWidth()){
                Icon(Icons.Rounded.Person, contentDescription = "Icono destino")
                Text(
                    modifier = Modifier.padding(10.dp,0.dp),
                    text = dataUser.nombre,
                    fontWeight = FontWeight.Medium,
                    fontSize = 15.sp
                )
            }
            Row (modifier = Modifier.fillMaxWidth()){
                Icon(Icons.Rounded.Email, contentDescription = "Icono destino")
                Text(
                    modifier = Modifier.padding(10.dp,0.dp),
                    text = dataUser.email,
                    fontWeight = FontWeight.Medium,
                    fontSize = 15.sp
                )
            }
        }
    }
}