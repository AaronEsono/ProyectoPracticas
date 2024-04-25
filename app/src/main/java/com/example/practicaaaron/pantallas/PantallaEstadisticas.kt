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
import androidx.compose.material3.Divider
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.practicaaaron.clases.utilidades.PieChart
import com.example.practicaaaron.ui.ViewModel.OpcionesViewModel
import java.util.Random

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ventanaEstadisticas(navHostController: NavHostController,opcionesViewModel: OpcionesViewModel){

    val state = rememberScrollState()
    val resultado = opcionesViewModel.resultadosTrabajadores.collectAsState()
    val mapa = remember { mutableMapOf<String,Int>() }
    val mapa2 = remember { mutableMapOf<String,Int>() }
    var colors:MutableList<Color> = remember {mutableListOf()}
    var terminado by remember {mutableStateOf(false)}


    LaunchedEffect (true){
        opcionesViewModel.resultadosTrabajadores()
    }

    LaunchedEffect (resultado.value.resultados.isNotEmpty()){
        Log.i("entro","${resultado.value.resultados}")
        val rnd = Random()
        resultado.value.resultados.forEach {
            mapa[it.nombre] = it.entregados
            mapa2[it.nombre] = it.incidencias
            colors.add(Color(rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256)))
            Log.i("Color","${colors.toString()}")
        }
        terminado = true
    }

    //Poner la informacion
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(0.dp, 60.dp, 0.dp, 0.dp)
            .verticalScroll(state),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        if(terminado){
            Text(text = "Estadística pedidos", fontSize = 25.sp, modifier = Modifier.padding(0.dp,15.dp))
            PieChart(data = mapa,colors = colors)

            Divider(thickness = 2.dp, color = Color.Black, modifier = Modifier.padding(0.dp,25.dp))

            Text(text = "Estadística incidencias", fontSize = 25.sp, modifier = Modifier.padding(0.dp,0.dp,0.dp,15.dp))
            PieChart(data = mapa2, incidencias = "incidencias", colors = colors)
        }
    }
}