package com.example.practicaaaron.pantallas

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.practicaaaron.ui.viewModel.TraspasosViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PantallaTraspasos(traspasosViewModel: TraspasosViewModel = hiltViewModel()){

    val pedidos = traspasosViewModel.pedidos.collectAsState().value
    val context = LocalContext.current

    Log.i("pedidos","$pedidos")

    LaunchedEffect (true){
        traspasosViewModel.getPedidosTraspasos(context)
    }
    
    Column (modifier = Modifier
        .fillMaxSize()
        .padding(0.dp, 60.dp, 0.dp, 0.dp)
        .verticalScroll(rememberScrollState())){
        pedidos.forEach { 
            Text(text = it.pedido.nombre)
        }
    }

}