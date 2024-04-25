package com.example.practicaaaron.navegador

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BusinessCenter
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
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.practicaaaron.R
import com.example.practicaaaron.pantallas.PantallaInfoProducto
import com.example.practicaaaron.pantallas.VentanaPrincipal
import com.example.practicaaaron.pantallas.ventanaLogin
import com.example.practicaaaron.pantallas.VentanaPerfil
import com.example.practicaaaron.pantallas.hecho
import com.example.practicaaaron.pantallas.pantallaMapa
import com.example.practicaaaron.pantallas.pantallaUsuarios
import com.example.practicaaaron.pantallas.ventanaEditarPerfil
import com.example.practicaaaron.pantallas.ventanaEntregaPedido
import com.example.practicaaaron.pantallas.ventanaEstadisticas
import com.example.practicaaaron.pantallas.ventanaPedidos
import com.example.practicaaaron.pantallas.ventanaPedidos2
import com.example.practicaaaron.ui.ViewModel.OpcionesViewModel
import com.example.practicaaaron.ui.theme.colorBarraEncima
import com.example.practicaaaron.ui.theme.colorPrimario
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

sealed class Pantallas(var route:String){
    data object Login : Pantallas("login")
    data object Pedidos : Pantallas("pedidos")
    data object Menu: Pantallas("menu")
    data object Perfil: Pantallas("perfil")
    data object Info: Pantallas("infoPedido")
    data object Editar: Pantallas("editar")
    data object Entregar:Pantallas("entregar")

    data object Rutas:Pantallas("rutas")
    data object Usuarios:Pantallas("usuarios")
    data object Hecho:Pantallas("Hecho")
    data object Estadistica:Pantallas("estadistica")
}

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavHost(
    navController: NavHostController,
    opcionesViewModel: OpcionesViewModel,
) {
    val showToolbar = remember {mutableStateOf(1)}

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // 1. todo, 2. solo Scaffold, 3. Navegacion
    hideOrShowToolbar(navController = navController, showToolbar = showToolbar)

    if(showToolbar.value == 3)
        interfazScaffold(navHostController = navController, showToolbar = showToolbar, opcionesViewModel)
    else if(showToolbar.value == 1)
        navegacion(navController = navController, opcionesViewModel = opcionesViewModel)
    else{
        barraArriba(
            navHostController = navController,
            showToolbar = showToolbar,
            opcionesViewModel = opcionesViewModel,
            scope = scope,
            drawerState = drawerState,
            scrollBehavior = scrollBehavior
        )
    }
}

//Funcion que controlar en qué pantallas se va a mostrar el scaffold
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun hideOrShowToolbar(
    navController: NavHostController,
    showToolbar: MutableState<Int>
){
    // Funcion que devuelve el destino actual cada vez que cambia
    navController.addOnDestinationChangedListener(NavController.OnDestinationChangedListener { controller, destination, arguments ->
        // Si estamos en el login, no mostrar el menú, en todas las demas pantallas sí
        when(controller.currentDestination?.route){
            Pantallas.Login.route ->{
                Log.i("entro","entro Aqui")
                showToolbar.value = 1
            }
            Pantallas.Rutas.route ->{
                showToolbar.value = 2
            }
            else ->{
                showToolbar.value = 3
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
    showToolbar: MutableState<Int>,
    opcionesViewModel: OpcionesViewModel
){
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(drawerContainerColor = colorBarraEncima) {
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
                            contentDescription = "Localized description",
                            modifier = Modifier.size(50.dp)
                        )
                    }
                }

                Column(modifier = Modifier
                    .padding(0.dp, 5.dp)
                    .fillMaxWidth(0.7f)) {
                    filaInformacionDrawer(navController = navHostController,drawerState,scope,Icons.Rounded.Home,"menu","Menú principal")
                    filaInformacionDrawer(navController = navHostController,drawerState,scope,Icons.Rounded.AccountCircle,"perfil","Mi perfil")
                    filaInformacionDrawer(navController = navHostController,drawerState,scope,Icons.Rounded.Close,"login","Cerrar sesión",opcionesViewModel)
                }
            }
        },
    ) {
        barraArriba(navHostController = navHostController, showToolbar = showToolbar, opcionesViewModel = opcionesViewModel, scope, drawerState, scrollBehavior)
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
            composable(Pantallas.Entregar.route){
                ventanaEntregaPedido(navController,opcionesViewModel)
            }
            composable(Pantallas.Rutas.route){
                pantallaMapa(navController,opcionesViewModel)
            }
            composable(Pantallas.Usuarios.route){
                pantallaUsuarios(navController,opcionesViewModel)
            }
            composable(Pantallas.Hecho.route){
                hecho(navHostController = navController, opcionesViewModel = opcionesViewModel)
            }
            composable(Pantallas.Estadistica.route){
                ventanaEstadisticas(navHostController = navController, opcionesViewModel = opcionesViewModel)
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
     Row(verticalAlignment = Alignment.CenterVertically,
         //Si el usuario le da a cerrar sesion, borrar todos los datos del viewModel del usuario
         modifier = Modifier
             .padding(15.dp, 10.dp)
             .clickable {
                 if (ruta == "login") {
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
             fontSize = 20.sp,
             fontWeight = FontWeight.Medium,
             modifier = Modifier
                 .padding(20.dp, 0.dp))
     }
}

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun barraArriba(
    navHostController: NavHostController,
    showToolbar: MutableState<Int>,
    opcionesViewModel: OpcionesViewModel,
    scope: CoroutineScope,
    drawerState: DrawerState,
    scrollBehavior: TopAppBarScrollBehavior
){
    var estado = opcionesViewModel.isLogged.collectAsState()

    Scaffold(
        topBar = {
                CenterAlignedTopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = colorPrimario,
                        titleContentColor = Color.White,
                    ),
                    title = {
                        Image(painter = painterResource(id = R.drawable.iconoapp), contentDescription = "iconoApp",modifier = Modifier
                            .clickable { navHostController?.navigate("menu") }
                            .size(75.dp))
                    },
                    navigationIcon = {
                        if (showToolbar.value == 3){
                            IconButton(onClick = {
                                scope.launch {
                                    drawerState.apply {
                                        if (isClosed) open() else close()
                                    }
                                }
                            }) {
                                Icon(
                                    imageVector = Icons.Filled.Menu,
                                    contentDescription = "Localized description",
                                    tint = Color.White,
                                    modifier = Modifier.size(50.dp)
                                )
                            }
                        }
                    },
                    scrollBehavior = scrollBehavior,

                    actions = {
                        if(estado.value == 2){
                            Icon(
                                imageVector = Icons.Filled.BusinessCenter,
                                contentDescription = "Localized description",
                                tint = Color.White,
                                modifier = Modifier.size(50.dp)
                            )
                        }
                    }
                )
        },
    ){
        navegacion(navController = navHostController, opcionesViewModel)
    }
}