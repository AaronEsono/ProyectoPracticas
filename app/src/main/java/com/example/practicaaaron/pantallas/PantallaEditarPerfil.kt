package com.example.practicaaaron.pantallas

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.Face
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.AbsoluteAlignment
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

//Modificar el diseño y los botones
@OptIn(ExperimentalComposeUiApi::class)
@Composable
@Preview
fun ventanaEditarPerfil(navHostController: NavHostController? = null){
    //Variables que guardan la informacion del label y si se ha clickeado la primera vez
    val estadoCampo1 by remember { mutableStateOf(false) }
    val mensajeCampo1 = remember { mutableStateOf("") }

    val estadoCampo2 by remember { mutableStateOf(false) }
    val mensajeCampo2 = remember { mutableStateOf("") }

    val (focusRequester) = FocusRequester.createRefs()

    Column (modifier = Modifier
        .fillMaxSize()
        .padding(0.dp, 60.dp), horizontalAlignment = Alignment.CenterHorizontally){
        Spacer(modifier = Modifier.padding(0.dp,20.dp))
        campoFuncion(
            campo = mensajeCampo1,
            firstTimeButton = estadoCampo1,
            labelTexto = "Editar usuario",
            textoError = "Rellena el campo",
            transformacion = VisualTransformation.None,
            teclado = KeyboardType.Ascii,
            accountCircle = Icons.Rounded.AccountCircle,
            focusRequester = focusRequester
        )

        campoFuncion(
            campo = mensajeCampo2,
            firstTimeButton = estadoCampo2,
            labelTexto = "Editar nombre",
            textoError = "Rellena el nombre",
            transformacion = VisualTransformation.None,
            teclado = KeyboardType.Ascii,
            accountCircle = Icons.Rounded.Face,
            focusRequester = focusRequester
        )

        Column (horizontalAlignment = AbsoluteAlignment.Left, modifier = Modifier
            .fillMaxWidth()
            .padding(30.dp, 10.dp)){
            ElevatedButton(onClick = { /*TODO*/ }, colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF4eb3cc)
            ),modifier = Modifier.size(200.dp,70.dp)) {
                Text("Cambiar contraseña", fontSize = 16.sp)
            }
        }

        Row (modifier = Modifier.fillMaxSize(), verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.SpaceEvenly){
            botonPerfil(navHostController = navHostController, ruta = "perfil", texto = "Cancelar")
            botonPerfil(navHostController = navHostController, ruta = "menu", texto = "Confirmar")
        }
    }
}