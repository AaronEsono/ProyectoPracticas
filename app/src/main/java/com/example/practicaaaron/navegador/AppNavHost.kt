package com.example.practicaaaron.navegador

import android.annotation.SuppressLint
import android.os.Build
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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

@Composable
fun hideOrShowToolbar(navController: NavController,showToolbar:MutableState<Boolean>){
    navController.addOnDestinationChangedListener(NavController.OnDestinationChangedListener { controller, destination, arguments ->
        when(controller.currentDestination?.route){
            Pantallas.Login.route ->{
                showToolbar.value = false
            }
            else ->{
                showToolbar.value = true
            }
        }
    })
}

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
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

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(modifier = Modifier.background(MaterialTheme.colorScheme.primaryContainer)) {
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
                    Row(modifier = Modifier.padding(0.dp, 20.dp).clickable {
                        navHostController?.navigate("menu")
                        scope.launch { drawerState.apply { close() } }
                    }) {
                        Icon(
                            Icons.Rounded.Home,
                            contentDescription = "PerfilIcono",
                            modifier = Modifier.size(40.dp)
                        )
                        Text(
                            text = "Menú principal",
                            fontSize = 25.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier
                                .padding(10.dp, 0.dp))
                    }
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.clickable {
                        navHostController?.navigate("perfil")
                        scope.launch { drawerState.apply { close() } }
                    }) {
                        Icon(
                            Icons.Rounded.AccountCircle,
                            contentDescription = "PerfilIcono",
                            modifier = Modifier.size(40.dp)
                        )
                        Text(
                            text = "Perfil",
                            fontSize = 25.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier
                                .padding(10.dp, 0.dp))
                    }
                    Row(modifier = Modifier.padding(0.dp, 20.dp).clickable {}) {
                        Icon(
                            Icons.Rounded.Close,
                            contentDescription = "PerfilIcono",
                            modifier = Modifier.size(40.dp)
                        )
                        Text(
                            text = "Cerrar sesión",
                            fontSize = 25.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier
                                .padding(10.dp, 0.dp))
                    }
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
                                "Nombre del usuario",
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
                ventanaPedidos(navHostController = navController)
            }
            composable(Pantallas.Menu.route){
                VentanaPrincipal(navHostController = navController,opcionesViewModel)
            }
            composable(Pantallas.Perfil.route){
                VentanaPerfil(navHostController = navController)
            }
            composable(Pantallas.Info.route){
                PantallaInfoProducto(navHostController = navController)
            }
            composable(Pantallas.Editar.route){
                ventanaEditarPerfil(navHostController = navController)
            }
        }
}