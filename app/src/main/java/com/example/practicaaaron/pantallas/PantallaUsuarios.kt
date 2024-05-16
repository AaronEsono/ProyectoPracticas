package com.example.practicaaaron.pantallas

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.practicaaaron.R
import com.example.practicaaaron.clases.entidades.DataUsuario
import com.example.practicaaaron.clases.usuarios.DataUser
import com.example.practicaaaron.clases.utilidades.AnimatedPreloader
import com.example.practicaaaron.ui.theme.colorPrimario
import com.example.practicaaaron.ui.viewModel.UsuariosViewModel
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PantallaUsuarios(navController: NavHostController,esConsulta:Boolean,usuariosViewModel: UsuariosViewModel = hiltViewModel()) {
    val usuarios = usuariosViewModel.usuarios.collectAsState().value

    val esConsultaVar = remember{ mutableStateOf(esConsulta) }
    val ruta = remember{ mutableStateOf("pedidos") }

    val context = LocalContext.current
    val done = remember{ mutableStateOf(false) }

    LaunchedEffect (true){
        usuariosViewModel.getUsuarios(context)
        if(esConsultaVar.value)
            ruta.value = "informacion"
        done.value = true
    }

    if(done.value){
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(0.dp, 60.dp, 0.dp, 0.dp)
        ) {
            items(usuarios) {
                CartaUsuario(it, navController,ruta)
                Spacer(modifier = Modifier.padding(0.dp, 4.dp))
                Divider(thickness = 3.dp, color = colorPrimario)
                Spacer(modifier = Modifier.padding(0.dp, 4.dp))
            }
        }
    }else{
        AnimatedPreloader(modifier = Modifier.size(100.dp), R.raw.animacioncargando, 1.0f)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CartaUsuario(
    user: DataUsuario,
    navController: NavHostController,
    ruta: MutableState<String>
) {

    Column(modifier = Modifier
        .fillMaxWidth()
        .height(IntrinsicSize.Max)
        .padding(0.dp, 10.dp)
        .clickable {
            if (ruta.value == "informacion") {
                navController.navigate("${ruta.value}/${user.idUsuario}")
            }else{
                navController.navigate("${ruta.value}/${LocalDate.now()}/${user.idUsuario}")
            }
        }) {

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = user.nombre, fontSize = 20.sp, fontWeight = FontWeight.Black)
        }

        FilaUsuario(user.username, Icons.Rounded.Person)
        FilaUsuario(user.email, Icons.Rounded.Email)
    }
}

@Composable
fun FilaUsuario(usuario: String = "Felipe", person: ImageVector) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp, 0.dp), verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(person, contentDescription = "")
        Spacer(modifier = Modifier.padding(10.dp, 0.dp))
        Text(text = usuario, fontSize = 14.sp)
    }
}