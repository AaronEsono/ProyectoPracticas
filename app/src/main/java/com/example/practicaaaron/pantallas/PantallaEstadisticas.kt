package com.example.practicaaaron.pantallas

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.practicaaaron.clases.utilidades.PieChart
import com.example.practicaaaron.ui.ViewModel.OpcionesViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ventanaEstadisticas(navHostController: NavHostController,opcionesViewModel: OpcionesViewModel){

    val state = rememberScrollState()
    val resultado = opcionesViewModel.resultadosTrabajadores.collectAsState()
    val idAdmin = opcionesViewModel.idUsuarioAdmin.collectAsState()
    val mapa = remember { mutableMapOf<String,Int>() }
    var terminado by remember {mutableStateOf(false)}
    var done = remember{ mutableStateOf(false) }
    var porcentajes = remember { mutableStateOf<MutableList<Int>>(mutableListOf()) }


    LaunchedEffect (true){
        opcionesViewModel.setId(idAdmin.value)
        opcionesViewModel.resultadosTrabajadores()
        Log.i("aver","$resultado")
        done.value = true
    }

    LaunchedEffect (done.value){
        Log.i("entro","${resultado.value.nombre.resultados}")
        mapa["entregados"] = resultado.value.nombre.resultados.entregados
        mapa["incidencias"] = resultado.value.nombre.resultados.incidencias
        mapa["sin entregar"] = resultado.value.nombre.resultados.sientregar

        porcentajes.value = mutableListOf()

        porcentajes.value.add(resultado.value.nombre.porcentajes.pEntregados)
        porcentajes.value.add(resultado.value.nombre.porcentajes.pIncidencias)
        porcentajes.value.add(resultado.value.nombre.porcentajes.pSinEntregar)

        terminado = true
    }

    //Poner la informacion
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(state),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        if(terminado){
            Text(text = "Estadística pedidos", fontSize = 25.sp, modifier = Modifier.padding(0.dp,15.dp))
            PieChart(data = mapa,porcentajes = porcentajes.value, total = resultado.value.nombre.pedidosTotales)
        }
    }
}