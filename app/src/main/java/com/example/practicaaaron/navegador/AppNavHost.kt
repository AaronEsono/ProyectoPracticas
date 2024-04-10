package com.example.practicaaaron.navegador

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.practicaaaron.pantallas.PantallaInfoProducto
import com.example.practicaaaron.pantallas.VentanaPrincipal
import com.example.practicaaaron.pantallas.ventanaLogin
import com.example.practicaaaron.pantallas.VentanaPerfil
import com.example.practicaaaron.pantallas.ventanaEditarPerfil
import com.example.practicaaaron.pantallas.ventanaPedidos
import com.example.practicaaaron.ui.ViewModel.OpcionesViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

sealed class Pantallas(var route:String){
    data object Login : Pantallas("login")
    data object Pedidos : Pantallas("pedidos")
    data object Menu: Pantallas("menu")
    data object Perfil: Pantallas("perfil")
    data object Info: Pantallas("infoPedido")
    data object Editar: Pantallas("editar")
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavHost(
    navController: NavHostController,
    opcionesViewModel: OpcionesViewModel,
) {
    val showToolbar = remember {mutableStateOf(false)}

    interfazScaffold(navHostController = navController, showToolbar = showToolbar, opcionesViewModel)
    hideOrShowToolbar(navController = navController, showToolbar = showToolbar)
}

//Funcion que controlar en qué pantallas se va a mostrar el scaffold
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun hideOrShowToolbar(
    navController: NavController,
    showToolbar: MutableState<Boolean>
){
    // Funcion que devuelve el destino actual cada vez que cambia
    navController.addOnDestinationChangedListener(NavController.OnDestinationChangedListener { controller, destination, arguments ->
        // Si estamos en el login, no mostrar el menú, en todas las demas pantallas sí
        when(controller.currentDestination?.route){
            Pantallas.Login.route ->{
                Log.i("entro","entro Aqui")
                showToolbar.value = false
            }
            else ->{
                showToolbar.value = true
            }
        }
    })
}

//Función composable que muestra la topAppBar de la aplicacion
@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "StateFlowValueCalledInComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun interfazScaffold(
    navHostController: NavHostController,
    showToolbar: MutableState<Boolean>,
    opcionesViewModel: OpcionesViewModel
){
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    //Variable para guardar el nombre del usuario logeado
    val nombre = opcionesViewModel.informacionUsuario.collectAsState().value

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(modifier = Modifier.background(MaterialTheme.colorScheme.primaryContainer)) {
                //Fila que muestra el icono para abrir el menú lateral y un texto indicativo
                Row(modifier = Modifier.padding(0.dp, 10.dp)) {
                    IconButton(onClick = {
                        scope.launch {
                            drawerState.apply {
                                if (isClosed) open() else close()
                            }
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Menu,
                            contentDescription = "Localized description"
                        )
                    }
                    Text(text = "Opciones", fontSize = 35.sp, fontWeight = FontWeight.Black)
                }

                Column(modifier = Modifier.padding(15.dp, 20.dp)) {
                    filaInformacionDrawer(navController = navHostController,drawerState,scope,Icons.Rounded.Home,"menu","Menú principal")
                    filaInformacionDrawer(navController = navHostController,drawerState,scope,Icons.Rounded.AccountCircle,"perfil","Mi perfil")
                    filaInformacionDrawer(navController = navHostController,drawerState,scope,Icons.Rounded.Close,"login","Cerrar sesion",opcionesViewModel)
                }
            }
        },
    ) {
        Scaffold(
            topBar = {
                if (showToolbar.value){

                    CenterAlignedTopAppBar(
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            titleContentColor = MaterialTheme.colorScheme.primary,
                        ),
                        title = {
                            Text(
                                "Bienvenido, ${nombre?.dataUser?.nombre}",
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.clickable { navHostController?.navigate("menu") }
                            )
                        },
                        navigationIcon = {
                            IconButton(onClick = {
                                scope.launch {
                                    drawerState.apply {
                                        if (isClosed) open() else close()
                                    }
                                }
                            }) {
                                Icon(
                                    imageVector = Icons.Filled.Menu,
                                    contentDescription = "Localized description"
                                )
                            }
                        },
                        scrollBehavior = scrollBehavior,
                    )
                }
            },
        ){
                navegacion(navController = navHostController, opcionesViewModel)
        }
}
}

//Funcion para navegar entre las distintas funciones
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun navegacion(navController: NavHostController, opcionesViewModel: OpcionesViewModel){
        NavHost(
            navController = navController,
            startDestination = Pantallas.Login.route
        ) {
            composable(Pantallas.Login.route){
                ventanaLogin(navHostController = navController,opcionesViewModel)
            }
            composable(Pantallas.Pedidos.route){
                ventanaPedidos(navHostController = navController, opcionesViewModel)
            }
            composable(Pantallas.Menu.route){
                VentanaPrincipal(navHostController = navController,opcionesViewModel)
            }
            composable(Pantallas.Perfil.route){
                VentanaPerfil(navHostController = navController, opcionesViewModel)
            }
            composable(Pantallas.Info.route){
                PantallaInfoProducto(navHostController = navController, opcionesViewModel)
            }
            composable(Pantallas.Editar.route){
                ventanaEditarPerfil(navHostController = navController)
            }
        }
}

//Funcion composable que muestra una fila con un icono y su descripcion de texto correspondiente
 @RequiresApi(Build.VERSION_CODES.O)
 @Composable
fun filaInformacionDrawer(
    navController: NavHostController,
    drawerState: DrawerState,
    scope: CoroutineScope,
    accountCircle: ImageVector,
    ruta: String,
    texto: String,
    opcionesViewModel: OpcionesViewModel? = null
){
     Row(modifier = Modifier
         .padding(0.dp, 10.dp)
            //Si el usuario le da a cerrar sesion, borrar todos los datos del viewModel del usuario
         .clickable {
             if(ruta== "login"){
                opcionesViewModel?.cerrarSesion()
             }
             navController?.navigate("$ruta")
             scope.launch { drawerState.apply { close() } }
         }) {
         Icon(
             accountCircle,
             contentDescription = "PerfilIcono",
             modifier = Modifier.size(40.dp)
         )
         Text(
             text = "$texto",
             fontSize = 25.sp,
             fontWeight = FontWeight.Medium,
             modifier = Modifier
                 .padding(10.dp, 0.dp))
     }
}