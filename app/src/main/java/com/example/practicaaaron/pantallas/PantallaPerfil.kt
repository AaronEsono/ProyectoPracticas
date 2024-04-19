package com.example.practicaaaron.pantallas

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountBox
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.practicaaaron.ui.ViewModel.OpcionesViewModel
import com.example.practicaaaron.ui.theme.colorPrimario
import com.example.practicaaaron.ui.theme.colorSecundario
import com.example.practicaaaron.ui.theme.colorTerciario
import java.time.format.TextStyle

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
@Preview
fun VentanaPerfil(
    navHostController: NavHostController? = null,
    opcionesViewModel: OpcionesViewModel? = null
) {
    // Variable que guarda la informacion del usuario
    var infoUsuario = opcionesViewModel?.informacionUsuario?.collectAsState()?.value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(0.dp, 60.dp, 0.dp, 0.dp)
            .verticalScroll(rememberScrollState())
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth().padding(0.dp,10.dp,0.dp,0.dp)
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                translate(left = 0f, top = -250f) {
                    drawCircle(colorPrimario, radius = 250.dp.toPx())
                }
            }
            Icon(
                Icons.Rounded.AccountCircle,
                contentDescription = "Imagen perfil",
                modifier = Modifier.size(130.dp)
            )

            Text(
                text = "${infoUsuario?.dataUser?.nombre}",
                fontSize = 40.sp,
                fontWeight = FontWeight.Black
            )
        }

        Spacer(modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 20.dp))
        informacion(
            campo = "Id_Usuario",
            valor = "${infoUsuario?.dataUser?.nombre}",
            Icons.Rounded.Person,
            20.sp
        )

        Divider(modifier = Modifier.padding(0.dp, 10.dp, 0.dp, 0.dp), color = Color.Gray)

        Spacer(modifier = Modifier.padding(0.dp, 10.dp))
        informacion(
            campo = "Usuario",
            valor = "${infoUsuario?.dataUser?.username}",
            icono = Icons.Rounded.AccountCircle,
            20.sp
        )

        Divider(modifier = Modifier.padding(0.dp, 10.dp, 0.dp, 0.dp), color = Color.Gray)

        Spacer(modifier = Modifier.padding(0.dp, 10.dp))
        informacion(
            campo = "Email",
            valor = "${infoUsuario?.dataUser?.email}",
            icono = Icons.Rounded.Email,
            20.sp
        )

        Divider(modifier = Modifier.padding(0.dp, 10.dp, 0.dp, 0.dp), color = Color.Gray)

        Spacer(modifier = Modifier.padding(0.dp, 10.dp))
        Column(
            verticalArrangement = Arrangement.Bottom,
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            botonPerfil(
                navHostController = navHostController,
                ruta = "editar",
                texto = "Editar perfil"
            )
        }
    }
}

//Funcion composable que muestra una fila con su texto correspondiente
@Composable
fun informacion(campo: String, valor: String, icono: ImageVector, sp: TextUnit = 28.sp) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(20.dp, 0.dp)) {
        Icon(
            icono, contentDescription = "$campo",
            Modifier
                .size(30.dp)
                .padding(0.dp, 0.dp)
        )
        Row(
            horizontalArrangement = Arrangement.Center, modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp, 0.dp, 25.dp, 0.dp)
        ) {
            Text(text = "$valor", fontSize = sp)
        }
    }
}

//Funcion composable que muestra un boton con el texto y ruta seleccionados
@Composable
fun botonPerfil(navHostController: NavHostController?, ruta: String, texto: String) {
    ElevatedButton(
        onClick = { navHostController?.navigate("$ruta") },
        modifier = Modifier.size(170.dp, 70.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = colorPrimario
        )
    ) {
        Text(text = "$texto", fontSize = 20.sp)
    }
}