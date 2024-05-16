package com.example.practicaaaron.pantallas

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.practicaaaron.R
import com.example.practicaaaron.clases.utilidades.AnimatedPreloader
import com.example.practicaaaron.clases.utilidades.PieChart
import com.example.practicaaaron.ui.viewModel.EstadisticasViewModel

@SuppressLint("SuspiciousIndentation", "MutableCollectionMutableState")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun VentanaEstadisticas(estadisticasViewModel: EstadisticasViewModel = hiltViewModel(), id:Int){

    val state = rememberScrollState()
    val informacion = estadisticasViewModel.informacion.collectAsState().value
    val context = LocalContext.current
    val terminado = remember {mutableStateOf(false)}

    val mapa = remember { mutableMapOf<String,Int>() }
    val porcentajes = remember { mutableStateOf<MutableList<Int>>(mutableListOf()) }

    LaunchedEffect (true){
        estadisticasViewModel.obtenerInformacion(id,context)
    }

    if( informacion.totales != -1){

        mapa[stringResource(id = R.string.entregados)] = informacion.entregados
        mapa[stringResource(id = R.string.incidencias)] = informacion.incidencias
        mapa[stringResource(id = R.string.sinEntre)] = informacion.sinEntregar

        porcentajes.value = mutableListOf()

        porcentajes.value.add(informacion.pEntregados)
        porcentajes.value.add(informacion.incidencias)
        porcentajes.value.add(informacion.pSinEntregar)

        terminado.value = true
    }
    //Poner la informacion
    if(terminado.value){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(state),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if(terminado.value){
                Text(text = stringResource(id = R.string.estadisticas2), fontSize = 25.sp, modifier = Modifier.padding(0.dp,15.dp))
                PieChart(data = mapa,porcentajes = porcentajes.value, total = informacion.totales)
            }else{
                Text(text = stringResource(id = R.string.noPed), fontSize = 12.sp, maxLines = 1)
            }
        }
    }else{
        AnimatedPreloader(modifier = Modifier.size(100.dp), R.raw.animacioncargando, 1.5f)
    }
}