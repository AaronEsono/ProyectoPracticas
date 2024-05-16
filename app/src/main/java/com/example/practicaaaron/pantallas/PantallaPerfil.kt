package com.example.practicaaaron.pantallas

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Person
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.practicaaaron.R
import com.example.practicaaaron.clases.utilidades.AnimatedPreloader
import com.example.practicaaaron.ui.theme.colorPrimario
import com.example.practicaaaron.ui.viewModel.PerfilViewModel

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun VentanaPerfil(
    perfilViewModel: PerfilViewModel = hiltViewModel()
) {
    // Variable que guarda la informacion del usuario
    val infoUsuario = perfilViewModel.usuario.collectAsState().value
    val tamano = (LocalConfiguration.current.screenHeightDp / 4 * -1.4)
    val done = remember { mutableStateOf(false) }

    LaunchedEffect (true){
        perfilViewModel.cogerDatos()
        done.value = true
    }

    if(done.value){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(0.dp, 60.dp, 0.dp, 0.dp)
                .verticalScroll(rememberScrollState())
        ) {

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(0.dp, 10.dp, 0.dp, 0.dp)
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    translate(left = 0f, top = tamano.toFloat()) {
                        drawCircle(colorPrimario, radius = 250.dp.toPx())
                    }
                }
                Icon(
                    Icons.Rounded.AccountCircle,
                    contentDescription = "Imagen perfil",
                    modifier = Modifier.size(130.dp),
                    tint = Color.White
                )

                Text(
                    text = infoUsuario.nombre,
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Black
                )
            }

            Spacer(modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 20.dp))
            Informacion(
                campo = "Id_Usuario",
                valor = "${infoUsuario.idUsuario}",
                Icons.Rounded.Person,
                20.sp
            )

            Divider(modifier = Modifier.padding(0.dp, 10.dp, 0.dp, 0.dp), color = Color.Gray)

            Spacer(modifier = Modifier.padding(0.dp, 10.dp))
            Informacion(
                campo = "Usuario",
                valor = infoUsuario.username,
                icono = Icons.Rounded.AccountCircle,
                20.sp
            )

            Divider(modifier = Modifier.padding(0.dp, 10.dp, 0.dp, 0.dp), color = Color.Gray)

            Spacer(modifier = Modifier.padding(0.dp, 10.dp))
            Informacion(
                campo = "Email",
                valor = infoUsuario.email,
                icono = Icons.Rounded.Email,
                20.sp
            )

            Divider(modifier = Modifier.padding(0.dp, 10.dp, 0.dp, 0.dp), color = Color.Gray)
        }
    }else{
        AnimatedPreloader(modifier = Modifier.size(100.dp), R.raw.animacioncargando, 1.0f)
    }
}

//Funcion composable que muestra una fila con su texto correspondiente
@Composable
fun Informacion(campo: String, valor: String, icono: ImageVector, sp: TextUnit = 28.sp) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(20.dp, 0.dp)) {
        Icon(
            icono, contentDescription = campo,
            Modifier
                .size(30.dp)
                .padding(0.dp, 0.dp)
        )
        Row(
            horizontalArrangement = Arrangement.Center, modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp, 0.dp, 25.dp, 0.dp)
        ) {
            Text(text = valor, fontSize = sp)
        }
    }
}