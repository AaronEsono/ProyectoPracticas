package com.example.practicaaaron.pantallas

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Call
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.PersonPin
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.practicaaaron.R
import com.example.practicaaaron.clases.pedidos.Cliente
import com.example.practicaaaron.clases.pedidos.PedidoLin
import com.example.practicaaaron.ui.viewModel.InfoProductoViewModel
import com.example.practicaaaron.ui.viewModel.OpcionesViewModel
import com.example.practicaaaron.ui.viewModel.loadImageFromBase64
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint(
    "UnusedMaterial3ScaffoldPaddingParameter", "UnrememberedMutableState",
    "SuspiciousIndentation"
)
@Composable
fun PantallaInfoProducto(
    navHostController: NavHostController,
    infoProductoViewModel: InfoProductoViewModel = hiltViewModel(),
    opcionesViewModel: OpcionesViewModel,
    idPedido: Int
) {

    val openAlertDialog = remember { mutableStateOf(false) }
    val state = rememberScrollState()

    val pedidoInfo = infoProductoViewModel.pedido.collectAsState().value

    val fecha = opcionesViewModel.fecha.collectAsState().value

    //Variable que guarda el pedido seleccionado por el cliente
    val pedido = opcionesViewModel.pedido.collectAsState().value

    val valorOpcion = remember { mutableStateOf("") }

    //Imagen relacionada con el pedido elegido
    val imagen = remember{ mutableStateOf(loadImageFromBase64(pedidoInfo.pedido.imagen)) }

    val esAdmin = opcionesViewModel.isLogged.collectAsState().value

    val entregado = opcionesViewModel.entregado.collectAsState().value
    val info = opcionesViewModel.informacion.collectAsState().value

    LaunchedEffect(true) {
        infoProductoViewModel.getPedido(idPedido)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(0.dp, 60.dp, 0.dp, 0.dp)
            .verticalScroll(state), horizontalAlignment = Alignment.CenterHorizontally
    ) {

        imagen.value?.let {
            Image(
                bitmap = it, contentDescription = "Descripcion de la imagen",
                modifier = Modifier
                    .size(120.dp)
                    .padding(10.dp, 10.dp, 0.dp, 0.dp), contentScale = ContentScale.FillHeight
            )
        }

        Text(text = "${pedido?.nombre}", fontSize = 30.sp, fontWeight = FontWeight.Black)

        Spacer(modifier = Modifier.padding(0.dp, 5.dp))
        VistaInformacionCliente(info = R.string.cliente, pedido?.cliente)

        VistaInformacionBulto(info = R.string.bultos, pedido?.bultos)

        Spacer(modifier = Modifier.padding(0.dp, 10.dp))

        if (esAdmin == 1 && fecha == LocalDate.now()) {
            Row(modifier = Modifier.padding(3.dp, 10.dp)) {
                BotonInfo(valor = R.string.confirmar, navHostController)
                Spacer(modifier = Modifier.padding(13.dp, 0.dp))

                Button(
                    onClick = { openAlertDialog.value = true },
                    modifier = Modifier.size(170.dp, 60.dp)
                ) {
                    Text(text = stringResource(id = R.string.marcar), fontSize = 13.sp)
                }
            }
        }

        when {
            openAlertDialog.value -> {
                AlertDialogExample(
                    onDismissRequest = { openAlertDialog.value = false },
                    onConfirmation = {
                        opcionesViewModel.updatePedido(valorOpcion.value)
                        opcionesViewModel.actualizarPedido(valorOpcion.value)
                        openAlertDialog.value = false
                    },
                    dialogTitle = stringResource(id = R.string.seleccione),
                    valorOpcion
                )
            }
        }
    }

    if (entregado?.retcode != -2) {
        opcionesViewModel.setInfo()

        val entregados = info.entregados.plus(info.incidencia)

        if (entregados >= (info.pedidos)) {
            navHostController.navigate("Hecho")
        } else {
            navHostController.navigate("pedidos")
        }
        opcionesViewModel.resetearEntrega()
    }
}

//Funcion composable que muestra todos los bultos de un pedido
@Composable
fun VistaInformacionBulto(info: Int, bultos: List<PedidoLin>?){

    Column (
        Modifier
            .padding(20.dp, 10.dp)
            .fillMaxWidth(0.95f)){
        Text(text = stringResource(id = info), fontWeight = FontWeight.Black, fontSize = 18.sp)

        bultos?.forEach {
            Spacer(modifier = Modifier.padding(0.dp,5.dp))
            InformacionBulto(it.refBulto,it.descripcion,it.unidades)
        }
    }
}

//Funcion composable que muestra toda la informacion del cliente
@Composable
fun VistaInformacionCliente(info: Int = 1, cliente: Cliente?){
        Column (modifier = Modifier
            .fillMaxWidth(0.95f)
            .padding(0.dp, 0.dp, 0.dp, 15.dp)){
            Text(text = stringResource(id = info), fontWeight = FontWeight.Black, fontSize = 18.sp, modifier = Modifier.padding(20.dp, 10.dp))

            CartaCliente(cliente)
        }
}


//Funcion que muestra un boton con el texto seleccionado
@Composable
fun BotonInfo(valor: Int, navHostController: NavHostController){
    Button(onClick = { navHostController.navigate("entregar") }, modifier = Modifier.size(170.dp,60.dp)) {
        Text(text = stringResource(id = valor), fontSize = 15.sp)
    }
}

//Funcion que muestra un dialogo cuando el usuario le da a la opcion de marcar incidencia
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AlertDialogExample(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogTitle: String,
    valorOpcion: MutableState<String>
) {
    Dialog(
        onDismissRequest = {
            onDismissRequest()
        }
    ){
        Card ( modifier = Modifier
            .fillMaxWidth()
            .height(420.dp)
            .padding(16.dp),
            shape = RoundedCornerShape(16.dp)) {

            Column (modifier = Modifier.fillMaxSize(),horizontalAlignment = Alignment.CenterHorizontally){
                Text(text = dialogTitle, fontSize = 23.sp, fontWeight = FontWeight.Medium
                    , modifier = Modifier.padding(0.dp, 10.dp))

                RadioButtonSample(valorOpcion)

                Row (verticalAlignment = Alignment.Bottom,modifier = Modifier
                    .fillMaxSize()
                    .padding(15.dp), horizontalArrangement = Arrangement.Absolute.Right){
                    TextButton(onClick = { onDismissRequest() }) {
                        Text(text = stringResource(id = R.string.cancelar), fontSize = 20.sp)
                    }

                    TextButton(onClick = { onConfirmation()}) {
                        Text(text = stringResource(id = R.string.confirmarBot), fontSize = 20.sp)
                    }
                }
            }
        }
    }
}

//Funcion con todos los posibles elecciones que va a tener el dialogo
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RadioButtonSample(valorOpcion: MutableState<String>) {
    val radioOptions = listOf("Ausente", "Rechazo", "Pérdida","dirección errónea")
    val (selectedOption, onOptionSelected) = remember { mutableStateOf(radioOptions[1] ) }

    valorOpcion.value = selectedOption
    LazyColumn{
        item { radioOptions.forEach { text ->
            Row(
                Modifier
                    .fillMaxWidth()
                    .selectable(selected = (text == selectedOption),
                        onClick = { onOptionSelected(text) })
            ) {
                RadioButton(
                    selected = (text == selectedOption),
                    onClick = { onOptionSelected(text) }
                )
                Text(
                    text = text,
                    modifier = Modifier
                        .padding(0.dp, 14.dp)
                )
            }
        }
        }
    }
}

//Funcion que muestra en forma de carta toda la informacion de un bulto
@Composable
@Preview
fun InformacionBulto(referencia:String = "Nombre", descripcion:String = "Esto es una descripcion", unidades:Int = 1){
    Card(
        colors = CardDefaults.cardColors(
            contentColor = Color.Black,
            containerColor = Color(0xFFc8cddb)
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(intrinsicSize = IntrinsicSize.Max)
    ) {
        Column (modifier = Modifier
            .fillMaxSize()
            .padding(6.dp, 0.dp, 6.dp, 8.dp)){
            Column (horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()){
                Text(
                    text = referencia,
                    modifier = Modifier
                        .padding(0.dp,5.dp),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Black
                )
            }

            Text(text = "Unidades: $unidades", fontWeight = FontWeight.Black)
            Text(text = descripcion, modifier = Modifier.padding(0.dp,0.dp,0.dp,5.dp))
        }
    }
}

//Funcion que muestra toda la informacion del cliente en una carta elevada
@Composable
fun CartaCliente(cliente: Cliente?) {
    Card(
        colors = CardDefaults.cardColors(
            contentColor = Color.Black,
            containerColor = Color(0xFFc8cddb)
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(intrinsicSize = IntrinsicSize.Max)
            .padding(20.dp, 0.dp)
    ) {
        Column (modifier = Modifier
            .fillMaxSize()
            .padding(6.dp, 0.dp, 6.dp, 8.dp)
            ){

            Spacer(modifier = Modifier.padding(0.dp,5.dp))
            FilaInformacion(Icons.Rounded.Person,"${cliente?.nombre}")

            Spacer(modifier = Modifier.padding(0.dp,5.dp))
            FilaInformacion(Icons.Rounded.PersonPin,"${cliente?.dni}")

            Spacer(modifier = Modifier.padding(0.dp,5.dp))
            FilaInformacion(Icons.Rounded.Call,"${cliente?.telefono}")

            Spacer(modifier = Modifier.padding(0.dp,5.dp))
            FilaInformacion(Icons.Rounded.LocationOn,"${cliente?.direccion?.poblacion}, ${cliente?.direccion?.municipio}, ${cliente?.direccion?.codigoPostal}")

            Spacer(modifier = Modifier.padding(0.dp,5.dp))
            FilaInformacion(Icons.Rounded.LocationOn,"${cliente?.direccion?.tipoCalle} ${cliente?.direccion?.nombreCalle}, ${cliente?.direccion?.portal} ${cliente?.direccion?.numero}")
        }
    }
}

//Funcion que muestra en una fila un icono con su descripcion, en este caso para el cliente
@Composable
fun FilaInformacion(icono: ImageVector, s: String) {
    Row (modifier = Modifier
        .fillMaxWidth()
        .padding(5.dp, 2.dp)){
        Icon(icono, contentDescription = "descripcion icono")
        Spacer(modifier = Modifier.padding(3.dp,0.dp))
        Text(text = s, modifier = Modifier.padding(0.dp,2.dp), fontWeight = FontWeight.Black, fontSize = 15.sp)
    }
}