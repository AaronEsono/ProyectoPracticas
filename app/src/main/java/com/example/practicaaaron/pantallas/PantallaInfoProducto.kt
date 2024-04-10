package com.example.practicaaaron.pantallas

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.Call
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import com.example.practicaaaron.clases.pedidos.Cliente
import com.example.practicaaaron.clases.pedidos.PedidoLin
import com.example.practicaaaron.ui.ViewModel.OpcionesViewModel

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "UnrememberedMutableState")
@Composable
@Preview
fun PantallaInfoProducto(
    navHostController: NavHostController? = null,
    opcionesViewModel: OpcionesViewModel? = null
){
    val openAlertDialog = remember { mutableStateOf(false) }
            val state = rememberScrollState()

            //Variable que guarda el pedido seleccionado por el cliente
            val pedido = opcionesViewModel?.pedido?.collectAsState()?.value

            val context = LocalContext.current
            //Imagen relacionada con el pedido elegido
            val imagen = pedido?.imagenDescripcion?.let { loadImageFromBase64(context, it) }

            Column (modifier = Modifier
                .fillMaxSize()
                .padding(0.dp, 60.dp, 0.dp, 0.dp)
                .verticalScroll(state), horizontalAlignment = Alignment.CenterHorizontally){

                if (imagen != null) {
                    Image(bitmap = imagen, contentDescription = "Descripcion de la imagen",
                        Modifier
                            .height(200.dp)
                            .fillMaxWidth(0.8f)
                            .padding(10.dp), contentScale = ContentScale.FillHeight)
                }

                Text(text = "${pedido?.nombre}", fontSize = 30.sp, fontWeight = FontWeight.Black)
                
                Spacer(modifier = Modifier.padding(0.dp,10.dp))
                vistaInformacionCliente(info = "Cliente", l = 0xFF49c6e6, pedido?.cliente)

                Spacer(modifier = Modifier.padding(0.dp,10.dp))
                vistaInformacionBulto(info = "Bultos",0xFFcf9cd9, pedido?.bultos)

                Spacer(modifier = Modifier.padding(0.dp,10.dp))
                Row (modifier = Modifier.padding(3.dp,10.dp)){
                    botonInfo(valor = "Confirmar Pedido")
                    Spacer(modifier = Modifier.padding(13.dp,0.dp))

                    Button(onClick = { openAlertDialog.value = true }, modifier = Modifier.size(170.dp,60.dp)) {
                        Text(text = "Marcar incidencia", fontSize = 13.sp)
                    }
                }

                when {
                    openAlertDialog.value -> {
                        AlertDialogExample(
                            onDismissRequest = { openAlertDialog.value = false },
                            onConfirmation = {
                                navHostController?.navigate("pedidos")
                                openAlertDialog.value = false
                            },
                            dialogTitle = "Seleccione incidencia",
                            opcionesViewModel
                        )
                    }
                }
            }
}

//Funcion composable que muestra todos los bultos de un pedido
@Composable
fun vistaInformacionBulto(info: String, l: Long, bultos: List<PedidoLin>?){

    Column (
        Modifier
            .background(Color(l))
            .padding(20.dp, 10.dp)
            .fillMaxWidth(0.95f)){
        Text(text = "Descripción: $info", fontWeight = FontWeight.Black, fontSize = 18.sp)

        bultos?.forEach {
            Spacer(modifier = Modifier.padding(0.dp,5.dp))
            informacionBulto(it.refBulto,it.descripcion,it.unidades)
        }
    }
}

//Funcion composable que muestra toda la informacion del cliente
@Composable
fun vistaInformacionCliente(info: String = "Cliente", l: Long = 0xFF49c6e6, cliente: Cliente?){
        Column (modifier = Modifier
            .fillMaxWidth(0.95f)
            .background(Color(l))
            .padding(0.dp, 0.dp, 0.dp, 15.dp)){
            Text(text = "$info", fontWeight = FontWeight.Black, fontSize = 18.sp, modifier = Modifier.padding(20.dp, 10.dp))

            cartaCliente(cliente)
        }
}


//Funcion que muestra un boton con el texto seleccionado
@Composable
fun botonInfo(valor:String){
    Button(onClick = { /*TODO*/ }, modifier = Modifier.size(170.dp,60.dp)) {
        Text(text = "$valor", fontSize = 15.sp)
    }
}

//Funcion que muestra un dialogo cuando el usuario le da a la opcion de marcar incidencia
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AlertDialogExample(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogTitle: String,
    opcionesViewModel: OpcionesViewModel?
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

                RadioButtonSample(opcionesViewModel)

                Row (verticalAlignment = Alignment.Bottom,modifier = Modifier
                    .fillMaxSize()
                    .padding(15.dp), horizontalArrangement = Arrangement.Absolute.Right){
                    TextButton(onClick = { onDismissRequest() }) {
                        Text(text = "Cancelar", fontSize = 20.sp)
                    }

                    TextButton(onClick = { onConfirmation() }) {
                        Text(text = "Confirmar", fontSize = 20.sp)
                    }
                }
            }
        }
    }
}

//Funcion con todos los posibles elecciones que va a tener el dialogo
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RadioButtonSample(opcionesViewModel: OpcionesViewModel?) {
    val radioOptions = listOf("Ausente", "Rechazo", "Pérdida","dirección errónea")
    val (selectedOption, onOptionSelected) = remember { mutableStateOf(radioOptions[1] ) }
    LazyColumn (){
        item { radioOptions.forEach { text ->
            Row(
                Modifier
                    .fillMaxWidth()
            ) {
                RadioButton(
                    selected = (text == selectedOption),
                    onClick = { onOptionSelected(text)
                    opcionesViewModel?.setIncidencia(text)
                    }
                )
                Text(
                    text = text,
                    modifier = Modifier
                        .padding(0.dp, 14.dp)
                        .clickable { onOptionSelected(text)
                            opcionesViewModel?.setIncidencia(text)}
                )
            }
        }
        }
    }
}

//Funcion que muestra en forma de carta toda la informacion de un bulto
@Composable
@Preview
fun informacionBulto(referencia:String = "Nombre", descripcion:String = "Esto es una descripcion", unidades:Int = 1){
    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
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
                    text = "$referencia",
                    modifier = Modifier
                        .padding(0.dp,5.dp),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Black
                )
            }

            Text(text = "Unidades: $unidades", fontWeight = FontWeight.Black)
            Text(text = "$descripcion", modifier = Modifier.padding(0.dp,0.dp,0.dp,5.dp))
        }
    }
}

//Funcion que muestra toda la informacion del cliente en una carta elevada
@Composable
fun cartaCliente(cliente: Cliente?) {
    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
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
            filaInformacion(Icons.Rounded.Person,"${cliente?.nombre}")

            Spacer(modifier = Modifier.padding(0.dp,5.dp))
            filaInformacion(Icons.Rounded.AccountCircle,"${cliente?.dni}")

            Spacer(modifier = Modifier.padding(0.dp,5.dp))
            filaInformacion(Icons.Rounded.Call,"${cliente?.telefono}")

            Spacer(modifier = Modifier.padding(0.dp,5.dp))
            filaInformacion(Icons.Rounded.LocationOn,"${cliente?.direccion?.poblacion}, ${cliente?.direccion?.municipio}, ${cliente?.direccion?.codigoPostal}")

            Spacer(modifier = Modifier.padding(0.dp,5.dp))
            filaInformacion(Icons.Rounded.LocationOn,"${cliente?.direccion?.tipoCalle} ${cliente?.direccion?.nombreCalle}, ${cliente?.direccion?.portal} ${cliente?.direccion?.numero}")
        }
    }
}

//Funcion que muestra en una fila un icono con su descripcion, en este caso para el cliente
@Composable
fun filaInformacion(icono: ImageVector, s: String) {
    Row (modifier = Modifier
        .fillMaxWidth()
        .padding(5.dp, 2.dp)){
        Icon(icono, contentDescription = "descripcion icono")
        Spacer(modifier = Modifier.padding(3.dp,0.dp))
        Text(text = "$s", modifier = Modifier.padding(0.dp,2.dp), fontWeight = FontWeight.Black, fontSize = 15.sp)
    }
}