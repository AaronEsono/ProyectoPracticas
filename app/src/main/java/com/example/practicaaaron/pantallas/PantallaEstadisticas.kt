package com.example.practicaaaron.pantallas

import android.annotation.SuppressLint
import android.os.Build
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.practicaaaron.R
import com.example.practicaaaron.clases.utilidades.PieChart
import com.example.practicaaaron.ui.viewModel.OpcionesViewModel

@SuppressLint("SuspiciousIndentation", "MutableCollectionMutableState")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun VentanaEstadisticas(opcionesViewModel: OpcionesViewModel){

    val state = rememberScrollState()
    val resultado = opcionesViewModel.resultadosTrabajadores.collectAsState()
    val idAdmin = opcionesViewModel.idUsuarioAdmin.collectAsState()
    val mapa = remember { mutableMapOf<String,Int>() }
    val terminado = remember {mutableStateOf(false)}
    val porcentajes = remember { mutableStateOf<MutableList<Int>>(mutableListOf()) }

    LaunchedEffect (true){
        opcionesViewModel.setId(idAdmin.value)
        opcionesViewModel.resultadosTrabajadores()
    }

    if( resultado.value.nombre.pedidosTotales != -1){

        mapa[stringResource(id = R.string.entregados)] = resultado.value.nombre.resultados.entregados
        mapa[stringResource(id = R.string.incidencias)] = resultado.value.nombre.resultados.incidencias
        mapa[stringResource(id = R.string.sinEntre)] = resultado.value.nombre.resultados.sientregar

        porcentajes.value = mutableListOf()

        porcentajes.value.add(resultado.value.nombre.porcentajes.pEntregados)
        porcentajes.value.add(resultado.value.nombre.porcentajes.pIncidencias)
        porcentajes.value.add(resultado.value.nombre.porcentajes.pSinEntregar)

        terminado.value = true
    }
    //Poner la informacion
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(state),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if(terminado.value){
            Text(text = stringResource(id = R.string.estadisticas2), fontSize = 25.sp, modifier = Modifier.padding(0.dp,15.dp))
            PieChart(data = mapa,porcentajes = porcentajes.value, total = resultado.value.nombre.pedidosTotales)
        }else{
            Text(text = stringResource(id = R.string.noPed), fontSize = 12.sp, maxLines = 1)
        }
    }
}