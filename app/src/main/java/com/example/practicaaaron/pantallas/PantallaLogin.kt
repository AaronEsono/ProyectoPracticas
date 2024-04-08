package com.example.practicaaaron.pantallas

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.practicaaaron.R
import com.example.practicaaaron.objetos.Data
import com.example.practicaaaron.objetos.DataUser
import com.example.practicaaaron.objetos.Usuario
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
@Preview
fun ventanaLogin(navHostController: NavHostController? = null){
    var campoEmail = remember { mutableStateOf("") }
    var campoContrasena = remember { mutableStateOf("") }
    var mensaje by remember { mutableStateOf("") }
    var firstTimeButton by remember { mutableStateOf(false) }

    val listColors = listOf(Color.Cyan, Color.Blue)

    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }

    Column (modifier = Modifier
        .fillMaxSize()
        .background(Brush.verticalGradient(listColors))
        .padding(0.dp, 50.dp, 0.dp, 0.dp), horizontalAlignment = Alignment.CenterHorizontally) {

        Image(painter = painterResource(id = R.drawable.icono), contentDescription = "Icono prueba"
            ,modifier= Modifier
                .padding(0.dp, 10.dp)
                .size(175.dp))

        Text(text = "Nombre empresa", fontSize = 34.sp,modifier = Modifier.padding(0.dp, 0.dp,0.dp,50.dp), fontWeight = FontWeight.Black,fontStyle = FontStyle.Italic)

        //Cambiar los colores de los errores a algo más legible
        Column(modifier = Modifier.padding(25.dp,0.dp)){
            campoFuncion(campoEmail,firstTimeButton,"Email","Rellene el email",accountCircle = Icons.Rounded.Email)
            Spacer(modifier = Modifier.padding(0.dp,10.dp))

            campoFuncion(campoContrasena,firstTimeButton,"Contraseña","Rellene la contraseña", PasswordVisualTransformation(),KeyboardType.Password,Icons.Rounded.Lock)
            Spacer(modifier = Modifier.padding(0.dp,10.dp))
        }

        Spacer(modifier = Modifier.padding(0.dp, 10.dp))
        ElevatedButton(onClick = {
            mensaje = verificar(campoEmail.value,campoContrasena.value)
            firstTimeButton = true
            showBottomSheet = true
        }, modifier = Modifier.size(250.dp,80.dp),shape = CutCornerShape(10)
        ) {
            Text("Entrar", fontSize = 25.sp)
        }
    }

    if(showBottomSheet && mensaje.isNotEmpty() && !mensaje.equals("Logeado!")){
        ModalBottomSheet(
            onDismissRequest = {
                showBottomSheet = false
            },
            sheetState = sheetState, modifier = Modifier.fillMaxHeight(0.2f)
        ) {
            Column (modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally){
                Text(text = "$mensaje", fontSize = 20.sp, fontWeight = FontWeight.Black, modifier = Modifier.padding(0.dp,10.dp))
            }
        }
    }

    if(mensaje == "Logeado!")
        navHostController?.navigate("menu")

}

@RequiresApi(Build.VERSION_CODES.O)
fun verificar(campoEmail: String, campoContrasena: String):String{
    var retorno = "Logeado!"
    val user = Data(DataUser(
        Usuario(1,1,"Prueba","1"
        ,"Aaron","1",true,
        LocalDate.now(), LocalDate.now()),0,""))

    if(campoEmail.isEmpty() || campoContrasena.isEmpty()){
        retorno = ""
    }else{
        if(campoEmail != user.dataUser.Usuario.Email)
            retorno = "No se ha encontrado el email"
        else if (campoContrasena != user.dataUser.Usuario.Email)
            retorno = "La contraseña es incorrecta"
    }

    return retorno
}

fun validacion(campo: String, firstTimeButton: Boolean):Boolean{
    return campo.isEmpty() && firstTimeButton
}

@Composable
fun campoFuncion(
    campo: MutableState<String>,
    firstTimeButton: Boolean,
    labelTexto: String,
    textoError: String,
    transformacion: VisualTransformation = VisualTransformation.None,
    teclado: KeyboardType = KeyboardType.Ascii,
    accountCircle: ImageVector
) {
    TextField(
        value = campo.value,
        onValueChange = {campo.value = it},
        label = { Text(labelTexto) },
        singleLine = true,
        isError = validacion(campo.value,firstTimeButton),
        supportingText = {
            if (validacion(campo.value,firstTimeButton)) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = textoError,
                    color = MaterialTheme.colorScheme.error
                    , fontSize = 22.sp
                )
            }
        },
        trailingIcon = {
            if(campo.value.isNotEmpty())
                IconButton(onClick = { campo.value = "" }) {
                    Icon(Icons.Filled.Close,"Borrar")
                }
        },
        leadingIcon = {
            var color = Color.Black
            if(validacion(campo.value,firstTimeButton))
                color = MaterialTheme.colorScheme.error

            Icon(accountCircle,"Usuario",tint = color)
        },
        visualTransformation = transformacion,
        keyboardOptions = KeyboardOptions(keyboardType = teclado),
    )
}