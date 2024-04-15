package com.example.practicaaaron.pantallas

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.graphics.Matrix
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.practicaaaron.R
import com.example.practicaaaron.clases.utilidades.LocationService
import com.example.practicaaaron.ui.ViewModel.OpcionesViewModel
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ventanaEntregaPedido(navController: NavHostController, opcionesViewModel: OpcionesViewModel) {

    //Variable para pillar una foto de la galeria
    var imageUri by remember {mutableStateOf<Uri?>(null)}

    //Variables para pillar una foto con la camara
    val content = LocalContext.current
    val img: Bitmap = BitmapFactory.decodeResource(Resources.getSystem(), android.R.drawable.ic_menu_report_image)

    //Variable que guarda la foto
    val imagenCamara = remember { mutableStateOf(img) }

    //Variable que almacena tanto la imagen de la galeria como la de la camara
    val pintador = remember { mutableStateOf(BitmapPainter(imagenCamara.value.asImageBitmap())) }

    // Variable que se inizializa cuando se va a sacar una foto
    val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.TakePicturePreview()) {
        if(it != null){

            imagenCamara.value = it
            pintador.value = BitmapPainter(imagenCamara.value.asImageBitmap())
        }
    }

    // Variable que se inicializa cuando se va a elegir una foto de la galeria
    val galleryLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()
        , onResult = {
                uri -> uri?.let { imageUri = it }
                //Transforma la imagen de la galeria de uri a bitmap
                transformar(imageUri,imagenCamara,content,pintador)
        })

    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()

    //Variables para realizar el scanner del codigo de barras
    val opciones = GmsBarcodeScannerOptions.Builder().setBarcodeFormats(
        Barcode.FORMAT_ALL_FORMATS
    ).build()

    val scanner = GmsBarcodeScanning.getClient(content,opciones)

    //Variable para guardar el estado del codigo de barras
    val valorBarras = remember { mutableStateOf("566487456") }

    Column(
        modifier = Modifier
            .padding(0.dp, 70.dp, 0.dp, 0.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ){

        Text(text = "Numero de pedido: Pedido 1", fontSize = 24.sp, fontWeight = FontWeight.Black)

        Spacer(modifier = Modifier.padding(0.dp,10.dp))

        obtenerImagenUsuario(painter = pintador)

        Button(onClick = {
            showBottomSheet = true
        }, modifier = Modifier.padding(20.dp,10.dp)) {
            Text(text = "Subir foto entrega")
        }

        Text(text = "${valorBarras.value}", fontSize = 25.sp)

        Spacer(modifier = Modifier.padding(0.dp,0.dp,0.dp,10.dp))

        Button(onClick = {scanner.startScan()
            .addOnSuccessListener { barcode: Barcode ->
                Toast.makeText(
                    content,
                    "SUCCESS: " + barcode.rawValue, Toast.LENGTH_LONG
                ).show()
                valorBarras.value = barcode.rawValue ?: "0000000000"
            }
            .addOnFailureListener { e: Exception ->
                Log.d(
                    "CODE_SCAN_FAILED",
                    "${e.message}"
                )
            }}) {
            Text(text = "Lectura código barras")
        }

        Column (verticalArrangement = Arrangement.Bottom, modifier = Modifier.fillMaxSize()){
            Row (horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp)){
                botonCancelarPedido(valor = "pedidos",navHostController = navController,texto = "cancelar")

                Button(onClick = {
                    if(valorBarras.value != "0000000000" && imagenCamara.value != img){
                        opcionesViewModel.hacerEntrega(imagenCamara.value,valorBarras.value,content)
                        navController.navigate("pedidos")

                    }else{
                        Toast.makeText(
                            content,
                            "Mete la foto y el codigo de barras", Toast.LENGTH_LONG
                        ).show()
                    }

                }, modifier = Modifier.size(130.dp,60.dp)) {
                    Text(text = "Confirmar")
                }
            }
        }
    }


        if(showBottomSheet){
            ModalBottomSheet(
                onDismissRequest = {
                    showBottomSheet = false
                },
                sheetState = sheetState, modifier = Modifier.fillMaxHeight(0.3f)
            ) {
                Row (modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly){
                    //Hacer en una funcion en un futuro, intertar meter las funciones en la funcion
                    Column (horizontalAlignment = Alignment.CenterHorizontally){
                        Image(painter = painterResource(id =R.drawable.galeriaicono), contentDescription = "edq",
                            modifier = Modifier
                                .size(80.dp)
                                .clickable {
                                    galleryLauncher.launch("image/*")
                                    showBottomSheet = false
                                })
                        Text(text = "Galeria")
                    }

                    Column (horizontalAlignment = Alignment.CenterHorizontally){
                        Image(painter = painterResource(id =R.drawable.camaraicono), contentDescription = "edq",
                            modifier = Modifier
                                .size(80.dp)
                                .clickable {
                                    launcher.launch()
                                    showBottomSheet = false
                                })
                        Text(text = "Foto")
                    }
                }
            }
        }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun botonCancelarPedido(
    valor: String,
    navHostController: NavHostController,
    texto: String,
){
    //Verificar que se ha metido una foto
    Button(onClick = {
            navHostController.navigate(valor)
        }, modifier = Modifier.size(130.dp,60.dp)) {
        Text(text = "$texto")
    }
}

@Composable
fun obtenerImagenUsuario(painter: MutableState<BitmapPainter>? = null){
    if (painter != null) {
        Image(painter = painter.value, contentDescription = "", modifier = Modifier
            .size(200.dp)
            .fillMaxWidth(0.9f)
            .background(Color.Black),contentScale = ContentScale.FillBounds)
    }

}

fun transformar(
    imageUri: Uri?,
    imagenGaleria: MutableState<Bitmap>,
    content: Context,
    pintador: MutableState<BitmapPainter>
) {
    imageUri?.let {
        if(Build.VERSION.SDK_INT < 28){
            imagenGaleria.value = MediaStore.Images.Media.getBitmap(content.contentResolver,it)
        }else{
            val source = ImageDecoder.createSource(content.contentResolver,it)
            imagenGaleria.value =  ImageDecoder.decodeBitmap(source)
        }
        imagenGaleria.value = rotateBitmap(imagenGaleria.value,90F)
        pintador.value = BitmapPainter(imagenGaleria.value.asImageBitmap())
    }
}

fun rotateBitmap(source: Bitmap, degrees: Float): Bitmap {
    val matrix = Matrix()
    matrix.postRotate(degrees)
    return Bitmap.createBitmap(
        source, 0, 0, source.width, source.height, matrix, true
    )
}