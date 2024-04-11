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
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.practicaaaron.R
import kotlinx.coroutines.CoroutineStart
import java.io.ByteArrayOutputStream
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun ventanaEntregaPedido() {

    //Variable para pillar una foto de la galeria
    var imageUri by remember {mutableStateOf<Uri?>(null)}

    //Variables para pillar una foto con la camara
    val content = LocalContext.current
    val img: Bitmap = BitmapFactory.decodeResource(Resources.getSystem(), android.R.drawable.ic_menu_report_image)

    //Variables que guardan las fotos
    val imagenCamara = remember { mutableStateOf(img) }
    val imagenGaleria = remember{ mutableStateOf(img) }

    //Variable que almacena tanto la imagen de la galeria como la de la camara
    val pintador = remember { mutableStateOf(BitmapPainter(imagenCamara.value.asImageBitmap())) }

    // Variable que se inizializa cuando se va a sacar una foto
    val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.TakePicturePreview()) {
        if(it != null){

            imagenCamara.value = it
            pintador.value = BitmapPainter(imagenCamara.value.asImageBitmap())
        }
    }

    // Variable que se inizializa cuando se va a elegir una foto de la galeria
    val galleryLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()
        , onResult = {
                uri -> uri?.let { imageUri = it }
                //Transforma la imagen de la galeria de uri a bitmap
                transformar(imageUri,imagenGaleria,content,pintador)
        })

    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()

    Column(
        modifier = Modifier
            .padding(0.dp, 70.dp, 0.dp, 0.dp)
            .fillMaxSize(),
    ){

        Column (horizontalAlignment = Alignment.CenterHorizontally, modifier=Modifier.fillMaxWidth()){
            Text(text = "Numero de pedido: Pedido 1", fontSize = 24.sp, fontWeight = FontWeight.Black)
        }

        Column (modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp, 10.dp), horizontalAlignment = Alignment.CenterHorizontally){
            obtenerImagenUsuario(painter = pintador)
        }

        Button(onClick = {
            showBottomSheet = true
        }, modifier = Modifier.padding(20.dp,0.dp)) {
            Text(text = "Subir foto entrega")
        }

        //Mirar lectura de barras y convertir de bitmap a base64

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
                            modifier = Modifier.size(80.dp)
                                .clickable {
                                    galleryLauncher.launch("image/*")
                                    showBottomSheet = false
                                })
                        Text(text = "Galeria")
                    }

                    Column (horizontalAlignment = Alignment.CenterHorizontally){
                        Image(painter = painterResource(id =R.drawable.camaraicono), contentDescription = "edq",
                            modifier = Modifier.size(80.dp)
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
}
@Composable
fun obtenerImagenUsuario(painter: MutableState<BitmapPainter>? = null){
    if (painter != null) {
        Image(painter = painter.value, contentDescription = "", modifier = Modifier
            .height(200.dp)
            .fillMaxWidth(0.9f)
            .background(Color.Black),contentScale = ContentScale.FillHeight)
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