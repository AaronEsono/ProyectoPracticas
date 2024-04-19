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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.practicaaaron.clases.usuarios.DataUser
import com.example.practicaaaron.ui.ViewModel.OpcionesViewModel
import com.example.practicaaaron.ui.theme.colorPrimario

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun pantallaUsuarios(navController: NavHostController, opcionesViewModel: OpcionesViewModel) {
    opcionesViewModel.obtenerTodos()
    var usuarios = opcionesViewModel?.usuarios?.collectAsState()?.value

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(0.dp, 60.dp, 0.dp, 0.dp)
    ) {
        if (usuarios != null) {
            items(usuarios.usuarios) {
                cartaUsuario(it, navController, opcionesViewModel)
                Spacer(modifier = Modifier.padding(0.dp, 4.dp))
                Divider(thickness = 3.dp, color = colorPrimario)
                Spacer(modifier = Modifier.padding(0.dp, 4.dp))
            }
        }
    }

}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun cartaUsuario(
    user: DataUser,
    navController: NavHostController,
    opcionesViewModel: OpcionesViewModel
) {

    Column(modifier = Modifier
        .fillMaxWidth()
        .height(IntrinsicSize.Max)
        .padding(0.dp, 10.dp)
        .clickable {
            opcionesViewModel.obtenerPedidos(user.idUsuario)
            navController.navigate("pedidos")
        }) {

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "${user.nombre}", fontSize = 20.sp, fontWeight = FontWeight.Black)
        }

        filaUsuario(user.username, Icons.Rounded.Person)
        filaUsuario(user.email, Icons.Rounded.Email)

    }

}

@Composable
fun filaUsuario(usuario: String = "Felipe", person: ImageVector) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp, 0.dp), verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(person, contentDescription = "")
        Spacer(modifier = Modifier.padding(10.dp, 0.dp))
        Text(text = "$usuario", fontSize = 14.sp)
    }
}