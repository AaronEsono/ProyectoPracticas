package com.example.practicaaaron.pantallas

import android.annotation.SuppressLint
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.practicaaaron.R
import com.example.practicaaaron.clases.usuarios.UsuarioLogin
import com.example.practicaaaron.ui.ViewModel.OpcionesViewModel
import com.example.practicaaaron.ui.theme.colorPrimario
import com.example.practicaaaron.ui.theme.colorSecundario
import com.example.practicaaaron.ui.theme.colorTerciario

@SuppressLint("StateFlowValueCalledInComposition")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
@Preview
fun ventanaLogin(
    navHostController: NavHostController? = null,
    opcionesViewModel: OpcionesViewModel? = null
) {

    //Variables que controlan el estado de los campos del login
    var campoUsername = remember { mutableStateOf("f.cambas") }
    var campoContrasena = remember { mutableStateOf("1234") }


    //Gradiente de colores para el color de fondo del login
    val listColors = listOf(colorSecundario, colorPrimario)

    // Variable que controla cuando se le ha dado por primera vez al boton
    var firstTimeButton by remember { mutableStateOf(false) }

    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }

    // Objeto que tiene los dos parametros del login
    var usuarioLogin by remember { mutableStateOf(UsuarioLogin()) }

    // Variable que controla si alguien se ha logeado correctamente o no
    var isLog = opcionesViewModel?.isLogged?.collectAsState()?.value

    // Variable que muestra el error en el login
    var mensaje = opcionesViewModel?.mensaje?.collectAsState()?.value

    val (focusRequester) = FocusRequester.createRefs()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.linearGradient(listColors)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Column(
            modifier = Modifier
                .fillMaxHeight(0.4f)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.iconoapp),
                contentDescription = "Icono prueba",
                modifier = Modifier
                    .padding(0.dp, 10.dp)
                    .size(210.dp)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()
                .padding(0.dp, 20.dp, 0.dp, 0.dp)
        ) {
            campoFuncion(
                campoUsername,
                firstTimeButton,
                "Usuario",
                "Rellene el usuario",
                accountCircle = Icons.Rounded.Person,
                focusRequester = focusRequester
            )
            Spacer(modifier = Modifier.padding(0.dp, 10.dp))

            campoFuncion(
                campoContrasena,
                firstTimeButton,
                "Contraseña",
                "Rellene la contraseña",
                PasswordVisualTransformation(),
                KeyboardType.Password,
                Icons.Rounded.Lock,
                focusRequester
            )
            Spacer(modifier = Modifier.padding(0.dp, 10.dp, 0.dp, 35.dp))

            ElevatedButton(
                onClick = {
                    firstTimeButton = true
                    showBottomSheet = true
                    hacerLlamada(usuarioLogin, campoContrasena, campoUsername, opcionesViewModel)
                }, modifier = Modifier
                    .height(60.dp)
                    .fillMaxWidth()
                    .padding(15.dp, 0.dp),
                shape = RoundedCornerShape(40.dp, 0.dp, 40.dp, 0.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorTerciario
                )
            ) {
                Text("Entrar", fontSize = 25.sp)
            }
        }
    }

    //Ventana modal que muestra el error encontrado en el login
    if (showBottomSheet && mensaje?.isNotEmpty() == true) {
        ModalBottomSheet(
            onDismissRequest = {
                showBottomSheet = false
            },
            sheetState = sheetState, modifier = Modifier.fillMaxHeight(0.3f)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(0.dp, 0.dp, 0.dp, 40.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "$mensaje",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Black,
                    modifier = Modifier.padding(0.dp, 10.dp)
                )
            }
        }
    }

    //Si se logea correctamente, pasar a la siguiente pantalla
    if (isLog != -1) {
        navHostController?.navigate("menu")
    }
}

// Funcion que retorna si se debe mostrar los errores en los label
fun validacion(campo: String, firstTimeButton: Boolean): Boolean {
    return campo.isEmpty() && firstTimeButton
}

// Funcion composable que muestra en textField con los parametros indicados
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun campoFuncion(
    campo: MutableState<String>,
    firstTimeButton: Boolean,
    labelTexto: String,
    textoError: String,
    transformacion: VisualTransformation = VisualTransformation.None,
    teclado: KeyboardType = KeyboardType.Ascii,
    accountCircle: ImageVector,
    focusRequester: FocusRequester
) {
    var tamano by remember { mutableStateOf(80.dp) }
    var showPassword by remember { mutableStateOf(teclado) }
    var passwordVisible by remember { mutableStateOf(false) }
    var campoUser = transformacion == VisualTransformation.None
    val nextStage = if (campoUser) ImeAction.Next else ImeAction.Done

    TextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp, 0.dp)
            .height(tamano)
            .focusRequester(focusRequester),
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = Color.Transparent,
            focusedContainerColor = Color.Transparent,

            unfocusedTextColor = Color.White,
            focusedTextColor = Color.White,

            unfocusedLabelColor = Color.White,
            focusedLabelColor = Color.White,

            unfocusedPlaceholderColor = Color.White,
            focusedPlaceholderColor = Color.White,

            unfocusedIndicatorColor = Color.White,
            focusedIndicatorColor = Color.White,

            cursorColor = Color.White,

            errorIndicatorColor = Color.White,
            errorContainerColor = Color.Transparent
        ),
        value = campo.value,
        onValueChange = { campo.value = it },
        label = { Text(labelTexto) },
        singleLine = true,
        isError = validacion(campo.value, firstTimeButton),
        supportingText = {
            if (validacion(campo.value, firstTimeButton)) {
                tamano = 100.dp
                Text(
                    text = textoError,
                    color = MaterialTheme.colorScheme.error, fontSize = 17.sp
                )
            } else {
                tamano = 80.dp
            }
        },
        trailingIcon = {
            if (campo.value.isNotEmpty() && teclado == KeyboardType.Ascii)
                IconButton(onClick = { campo.value = "" }) {
                    Icon(Icons.Filled.Close, "Borrar", tint = Color.White)
                }
            else if (campo.value.isNotEmpty() && teclado == KeyboardType.Password) {
                var image = if (passwordVisible)
                    Icons.Filled.VisibilityOff
                else Icons.Filled.Visibility

                val description = if (passwordVisible) "Hide password" else "Show password"

                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(imageVector = image, description, tint = Color.White)
                }
            }
        },
        leadingIcon = {
            var color = Color.White
            if (validacion(campo.value, firstTimeButton))
                color = MaterialTheme.colorScheme.error

            Icon(accountCircle, "Usuario", tint = color)
        },
        visualTransformation = if (campoUser) transformacion else if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = showPassword, imeAction = nextStage),
    )
}

@RequiresApi(Build.VERSION_CODES.O)
fun hacerLlamada(
    usuarioLogin: UsuarioLogin,
    campoContrasena: MutableState<String>,
    campoUsername: MutableState<String>,
    opcionesViewModel: OpcionesViewModel?
) {
    usuarioLogin.password = campoContrasena.value
    usuarioLogin.username = campoUsername.value

    //Si los campos no estan vacios, se hace la peticion
    if (usuarioLogin.password.isNotEmpty() && usuarioLogin.username.isNotEmpty())
        opcionesViewModel?.hacerLogin(usuarioLogin)
}