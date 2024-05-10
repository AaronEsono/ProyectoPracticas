package com.example.practicaaaron.navegador

import android.annotation.SuppressLint
import android.os.Build
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
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Menu
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.practicaaaron.R
import com.example.practicaaaron.pantallas.PantallaInfoProducto
import com.example.practicaaaron.pantallas.VentanaEntregaPedido
import com.example.practicaaaron.pantallas.VentanaPrincipal
import com.example.practicaaaron.pantallas.VentanaLogin
import com.example.practicaaaron.pantallas.VentanaPerfil
import com.example.practicaaaron.pantallas.Hecho
import com.example.practicaaaron.pantallas.PantallaMapa
import com.example.practicaaaron.pantallas.PantallaMenuFuturo
import com.example.practicaaaron.pantallas.PantallaUsuarios
import com.example.practicaaaron.pantallas.VentanaEstadisticas
import com.example.practicaaaron.pantallas.VentanaPedidos
import com.example.practicaaaron.ui.viewModel.OpcionesViewModel
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
    data object Entregar:Pantallas("entregar")
    data object Rutas:Pantallas("rutas")
    data object Usuarios:Pantallas("usuarios")
    data object Hecho:Pantallas("Hecho")
    data object Estadistica:Pantallas("estadistica")
    data object Informacion:Pantallas("informacion")
    data object Futuro:Pantallas("futuro")
}

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavHost(
    navController: NavHostController,
    opcionesViewModel: OpcionesViewModel,
) {
    val showToolbar = remember { mutableIntStateOf(1) }

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // 1. todo, 2. solo Scaffold, 3. Navigation
    HideOrShowToolbar(navController = navController, showToolbar = showToolbar)

    when (showToolbar.intValue) {
        3 ->
            InterfazScaffold(navHostController = navController,showToolbar = showToolbar,opcionesViewModel)
        1 ->
            Navegacion(navController = navController, opcionesViewModel = opcionesViewModel)
        else ->
            BarraArriba(
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
fun HideOrShowToolbar(
    navController: NavHostController,
    showToolbar: MutableState<Int>
){
    // Funcion que devuelve el destino actual cada vez que cambia
    navController.addOnDestinationChangedListener { controller, _, _ ->
        // Si estamos en el login, no mostrar el menú, en todas las demas pantallas sí
        when (controller.currentDestination?.route) {
            Pantallas.Login.route -> {
                showToolbar.value = 1
            }
            Pantallas.Rutas.route -> {
                showToolbar.value = 2
            }
            else -> {
                showToolbar.value = 3
            }
        }
    }
}

//Función composable que muestra la topAppBar de la aplicacion
@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "StateFlowValueCalledInComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InterfazScaffold(
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
                    FilaInformacionDrawer(navController = navHostController,drawerState,scope,Icons.Rounded.Home,"menu",R.string.menu,null)
                    FilaInformacionDrawer(navController = navHostController,drawerState,scope,Icons.Rounded.AccountCircle,"perfil",R.string.perfil,null)
                    FilaInformacionDrawer(navController = navHostController,drawerState,scope,Icons.Rounded.Close,"login",R.string.cerrar,opcionesViewModel)
                }
            }
        },
    ) {
        BarraArriba(navHostController = navHostController, showToolbar = showToolbar, opcionesViewModel = opcionesViewModel, scope, drawerState, scrollBehavior)
}
}

//Funcion para navegar entre las distintas funciones
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Navegacion(navController: NavHostController, opcionesViewModel: OpcionesViewModel){
        NavHost(
            navController = navController,
            startDestination = Pantallas.Login.route
        ) {
            composable(Pantallas.Login.route){
                VentanaLogin(navHostController = navController,opcionesViewModel)
            }
            composable(Pantallas.Pedidos.route){
                VentanaPedidos(navHostController = navController, opcionesViewModel)
            }
            composable(Pantallas.Menu.route){
                VentanaPrincipal(navHostController = navController,opcionesViewModel)
            }
            composable(Pantallas.Perfil.route){
                VentanaPerfil(opcionesViewModel)
            }
            composable(Pantallas.Info.route){
                PantallaInfoProducto(navHostController = navController, opcionesViewModel)
            }
            composable(Pantallas.Entregar.route){
                VentanaEntregaPedido(navController,opcionesViewModel)
            }
            composable(Pantallas.Rutas.route){
                PantallaMapa(opcionesViewModel)
            }
            composable(Pantallas.Usuarios.route){
                opcionesViewModel.setEstadistica(false)
                PantallaUsuarios(navController,opcionesViewModel)
            }
            composable(Pantallas.Hecho.route){
                Hecho(navHostController = navController)
            }
            composable(Pantallas.Estadistica.route){
                opcionesViewModel.setEstadistica(true)
                PantallaUsuarios(navController,opcionesViewModel)
            }
            composable(Pantallas.Informacion.route){
                VentanaEstadisticas(opcionesViewModel = opcionesViewModel)
            }
            composable(Pantallas.Futuro.route){
                PantallaMenuFuturo(navController,opcionesViewModel)
            }
        }
}

//Funcion composable que muestra una fila con un icono y su descripcion de texto correspondiente
 @RequiresApi(Build.VERSION_CODES.O)
 @Composable
fun FilaInformacionDrawer(
    navController: NavHostController,
    drawerState: DrawerState,
    scope: CoroutineScope,
    accountCircle: ImageVector,
    ruta: String,
    texto: Int,
    opcionesViewModel: OpcionesViewModel? = null
){
     Row(verticalAlignment = Alignment.CenterVertically,
         //Si el usuario le da a cerrar sesion, borrar todos los datos del viewModel del usuario
         modifier = Modifier
             .padding(15.dp, 10.dp)
             .clickable {
                 if (ruta == "login") {
                     opcionesViewModel?.mandarCerrarSesion()
                     opcionesViewModel?.cerrarSesion()
                 }
                 navController.navigate(ruta)
                 scope.launch { drawerState.apply { close() } }
             }) {
         Icon(
             accountCircle,
             contentDescription = stringResource(id = R.string.iconoApp),
             modifier = Modifier.size(40.dp)
         )
         Text(
             text = stringResource(id = texto),
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
fun BarraArriba(
    navHostController: NavHostController,
    showToolbar: MutableState<Int>,
    opcionesViewModel: OpcionesViewModel,
    scope: CoroutineScope,
    drawerState: DrawerState,
    scrollBehavior: TopAppBarScrollBehavior
){
    val estado = opcionesViewModel.isLogged.collectAsState()

    Scaffold(
        topBar = {
                CenterAlignedTopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = colorPrimario,
                        titleContentColor = Color.White,
                    ),
                    title = {
                        Image(painter = painterResource(id = R.drawable.iconoapp), contentDescription = stringResource(id = R.string.iconoApp)
                            ,modifier = Modifier
                            .clickable { navHostController.navigate("menu") }
                            .size(75.dp))
                    },
                    navigationIcon = {
                        if (showToolbar.value == 3){
                            val icono = remember { mutableStateOf(Icons.Rounded.Menu) }
                            navHostController.addOnDestinationChangedListener { controller, _, _ ->
                                when (controller.currentDestination?.route) {
                                    Pantallas.Menu.route -> {
                                        icono.value = Icons.Rounded.Menu
                                    }

                                    else -> {
                                        icono.value = Icons.Rounded.ArrowBackIosNew
                                    }
                                }
                            }
                            IconButton(onClick = {
                                scope.launch {
                                    if(icono.value == Icons.Rounded.Menu){
                                        drawerState.apply {
                                            if (isClosed) open() else close()
                                        }
                                    }else{
                                        navHostController.popBackStack()
                                    }
                                }
                            }) {
                                Icon(
                                    imageVector = icono.value,
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
        Navegacion(navController = navHostController, opcionesViewModel)
    }
}