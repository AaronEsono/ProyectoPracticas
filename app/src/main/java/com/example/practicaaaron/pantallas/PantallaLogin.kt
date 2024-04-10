package com.example.practicaaaron.pantallas

import android.annotation.SuppressLint
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
import androidx.compose.material.icons.rounded.Person
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
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.practicaaaron.R
import com.example.practicaaaron.clases.usuarios.Data
import com.example.practicaaaron.clases.usuarios.UsuarioLogin
import com.example.practicaaaron.ui.ViewModel.OpcionesViewModel

@SuppressLint("StateFlowValueCalledInComposition")
@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ventanaLogin(navHostController: NavHostController? = null, opcionesViewModel: OpcionesViewModel){

    //Variables que controlan el estado de los campos del login
    var campoUsername = remember { mutableStateOf("") }
    var campoContrasena = remember { mutableStateOf("") }

    //Gradiente de colores para el color de fondo del login
    val listColors = listOf(Color.Cyan, Color.Blue)

    // Variable que controla cuando se le ha dado por primera vez al boton
    var firstTimeButton by remember { mutableStateOf(false) }

    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }

    // Objeto que tiene los dos parametros del login
    var usuarioLogin by remember { mutableStateOf(UsuarioLogin()) }

    // Variable que controla si alguien se ha logeado correctamente o no
    var isLog = opcionesViewModel.isLogged.collectAsState().value

    // Variable que muestra el error en el login
    var mensaje = opcionesViewModel.mensaje.collectAsState().value

    Column (modifier = Modifier
        .fillMaxSize()
        .background(Brush.verticalGradient(listColors))
        .padding(0.dp, 40.dp, 0.dp, 0.dp), horizontalAlignment = Alignment.CenterHorizontally) {

        Image(painter = painterResource(id = R.drawable.icono), contentDescription = "Icono prueba"
            ,modifier= Modifier
                .padding(0.dp, 10.dp)
                .size(175.dp))

        Text(text = "Nombre empresa", fontSize = 34.sp,modifier = Modifier.padding(0.dp, 0.dp,0.dp,50.dp), fontWeight = FontWeight.Black,fontStyle = FontStyle.Italic)

        Column(modifier = Modifier.padding(25.dp,0.dp)){
            campoFuncion(campoUsername,firstTimeButton,"Usuario","Rellene el usuario",accountCircle = Icons.Rounded.Person)
            Spacer(modifier = Modifier.padding(0.dp,10.dp))

            campoFuncion(campoContrasena,firstTimeButton,"Contraseña","Rellene la contraseña", PasswordVisualTransformation(),KeyboardType.Password,Icons.Rounded.Lock)
            Spacer(modifier = Modifier.padding(0.dp,10.dp))
        }

        Spacer(modifier = Modifier.padding(0.dp, 10.dp))
        ElevatedButton(onClick = {
            firstTimeButton = true
            showBottomSheet = true
            usuarioLogin.password = campoContrasena.value
            usuarioLogin.username = campoUsername.value

            //Si los campos no estan vacios, se hace la peticion
            if(usuarioLogin.password.isNotEmpty() && usuarioLogin.username.isNotEmpty())
                opcionesViewModel.hacerLogin(usuarioLogin)

        }, modifier = Modifier.size(250.dp,80.dp),shape = CutCornerShape(10)
        ) {
            Text("Entrar", fontSize = 25.sp)
        }
    }

    //Ventana modal que muestra el error encontrado en el login
    if(showBottomSheet && mensaje?.isNotEmpty() == true){
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

    //Si se logea correctamente, pasar a la siguiente pantalla
    if(isLog == true){
        navHostController?.navigate("menu")
    }
}

// Funcion que retorna si se debe mostrar los errores en los label
fun validacion(campo: String, firstTimeButton: Boolean):Boolean{
    return campo.isEmpty() && firstTimeButton
}

// Funcion composable que muestra en textField con los parametros indicados
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