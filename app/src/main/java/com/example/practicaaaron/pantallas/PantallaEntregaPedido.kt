package com.example.practicaaaron.pantallas

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
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
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Photo
import androidx.compose.material.icons.rounded.PhotoCamera
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.practicaaaron.R
import com.example.practicaaaron.ui.viewModel.EntregaViewModel
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions
import io.github.joelkanyi.sain.Sain
import io.github.joelkanyi.sain.SignatureAction
import io.github.joelkanyi.sain.SignatureState
import java.time.LocalDate

@SuppressLint("RememberReturnType")
@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VentanaEntregaPedido(
    navController: NavHostController,
    entregaViewModel: EntregaViewModel = hiltViewModel(),
    id: Int,
    fecha: LocalDate
) {

    //Variable para pillar una foto de la galeria
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    //Variables para pillar una foto con la camara
    val content = LocalContext.current
    val img = remember { mutableStateOf<Bitmap>(BitmapFactory.decodeResource(Resources.getSystem(), android.R.drawable.ic_menu_report_image)) }

    //Variable que guarda la foto
    val imagenCamara = remember { mutableStateOf(img.value) }

    val imagenHecha = remember{ mutableStateOf(false) }

    //Variable que almacena tanto la imagen de la galeria como la de la camara
    val pintador = remember { mutableStateOf(BitmapPainter(imagenCamara.value.asImageBitmap())) }

    // Variable que se inizializa cuando se va a sacar una foto
    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.TakePicturePreview()) {
            if (it != null) {
                imagenCamara.value = it
                pintador.value = BitmapPainter(imagenCamara.value.asImageBitmap())
                imagenHecha.value = true
            }
        }

    // Variable que se inicializa cuando se va a elegir una foto de la galeria
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            uri?.let { imageUri = it }
            //Transforma la imagen de la galeria de uri a bitmap
            transformar(imageUri, imagenCamara, content, pintador)
            imagenHecha.value = true
            Log.i("entroGaleria","Hola")
        })

    val showBottomSheet = remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()

    //Variable para guardar el estado del codigo de barras
    val valorBarras = remember { mutableStateOf("0000000000") }

    val nombre = entregaViewModel.nombre.collectAsState().value
    val idUser = entregaViewModel.id.collectAsState().value

    val state = rememberScrollState()

    //Variable que guarda la firma
    var imageBitmap: ImageBitmap? by remember {
        mutableStateOf(null)
    }

    val stateFirma = remember {
        SignatureState()
    }

    //Variables para iniciar el codigo de barras
    val barCodeLanzador = rememberLauncherForActivityResult(ScanContract()) { result ->
        if (result.contents == null) {
            Toast.makeText(content, "Cancelled", Toast.LENGTH_SHORT).show()
        } else {
            valorBarras.value = result.contents
        }
    }

    val requestPermissionLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                showCamera(barCodeLanzador)
            }
        }

    val colorBorde = remember { mutableStateOf(Color.Black) }

    val entrega = entregaViewModel.entrega.collectAsState().value
    LaunchedEffect (true){
        entregaViewModel.getName(id)
        entregaViewModel.getId()
    }

    if(nombre.isNotEmpty()){
        Column(
            modifier = Modifier
                .padding(0.dp, 60.dp, 0.dp, 0.dp)
                .fillMaxSize()
                .verticalScroll(state),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(text = nombre, fontSize = 24.sp, fontWeight = FontWeight.Black)

            Spacer(modifier = Modifier.padding(0.dp, 10.dp))

            ObtenerImagenUsuario(painter = pintador, showBottomSheet)

            Spacer(modifier = Modifier.padding(0.dp, 10.dp))
            Divider()

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp, 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(onClick = {
                    checkCameraPermission(content, barCodeLanzador, requestPermissionLauncher)
                }, modifier = Modifier.height(50.dp)) {
                    Text(text = stringResource(id = R.string.lectura))
                }
                Text(text = valorBarras.value, fontSize = 25.sp)
            }

            Divider()

            Spacer(modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 10.dp))

            Text(text = stringResource(id = R.string.firma), fontSize = 17.sp, modifier = Modifier.padding(0.dp, 10.dp))
            //Funcion con la que dibujas la firma
            Column(
                modifier = Modifier.fillMaxWidth(0.9f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Sain(
                    state = stateFirma,
                    signatureThickness = 1.dp,
                    modifier = Modifier
                        .fillMaxWidth(1f)
                        .padding(10.dp, 0.dp)
                        .height(250.dp)
                        .border(
                            BorderStroke(
                                width = 2.dp,
                                color = colorBorde.value
                            ),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(0.dp, 10.dp),
                    onComplete = { signatureBitmap ->
                        if (signatureBitmap != null) {
                            colorBorde.value = Color.Green
                            imageBitmap = signatureBitmap
                        } else {
                            println("La firma está vacia")
                        }
                    },
                ) { action ->
                    Row(
                        modifier = Modifier
                            .padding(top = 16.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        if(imageBitmap != null){
                            Button(
                                modifier = Modifier.weight(1f),
                                onClick = {
                                    imageBitmap = null
                                    colorBorde.value = Color.Black
                                    action(SignatureAction.CLEAR)
                                }) {
                                Text(text = stringResource(id = R.string.limpiar))
                            }
                        }else{
                            action(SignatureAction.COMPLETE)
                            Spacer(modifier = Modifier.padding(top = 48.dp))
                        }
                    }
                }
            }

            if(entrega != -1){
                if(entrega == 0){
                    navController.navigate("Hecho/${idUser}/${fecha}")
                }else{
                    navController.navigate("pedidos/${fecha}/${idUser}")
                }
            }

            Column(verticalArrangement = Arrangement.Bottom, modifier = Modifier.fillMaxSize()) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    BotonCancelarPedido(
                        valor = "pedidos",
                        navHostController = navController,
                        texto = stringResource(id = R.string.cancelar),
                        id = idUser,
                        fecha = fecha
                    )

                    //&& valorBarras.value != "0000000000" && imagenHecha.value //esto va dentro del if
                    Button(onClick = {
                        Log.i("etiqueta", "${imagenCamara.value}")
                        if (imageBitmap != null && valorBarras.value != "0000000000" && imagenHecha.value) {
                            entregaViewModel.hacerEntrega(imagenCamara.value, valorBarras.value, content,imageBitmap!!,id)
                        } else {
                            Toast.makeText(
                                content,
                                "Mete la foto, el código de barras y la firma.", Toast.LENGTH_LONG
                            ).show()
                        }

                    }, modifier = Modifier.size(130.dp, 60.dp)) {
                        Text(text = stringResource(id = R.string.confirmarBot))
                    }
                }
            }
        }

        if (showBottomSheet.value) {
            ModalBottomSheet(
                onDismissRequest = {
                    showBottomSheet.value = false
                },
                sheetState = sheetState, modifier = Modifier.fillMaxHeight(0.3f)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    //Hacer en una funcion en un futuro, intertar meter las funciones en la funcion
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            Icons.Rounded.Photo,
                            contentDescription = "foto galeria",
                            modifier = Modifier
                                .size(80.dp)
                                .clickable {
                                    galleryLauncher.launch("image/*")
                                    showBottomSheet.value = false
                                })
                        Text(text = stringResource(id = R.string.galeria))
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Rounded.PhotoCamera,
                            contentDescription = "foto camara",
                            modifier = Modifier
                                .size(80.dp)
                                .clickable {
                                    launcher.launch()
                                    showBottomSheet.value = false
                                })
                        Text(text = stringResource(id = R.string.foto))
                    }
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun BotonCancelarPedido(
    valor: String,
    navHostController: NavHostController,
    texto: String,
    id:Int,
    fecha:LocalDate
) {
    //Verificar que se ha metido una foto
    Button(onClick = {
        navHostController.navigate("$valor/$fecha/$id")
    }, modifier = Modifier.size(130.dp, 60.dp)) {
        Text(text = texto)
    }
}

@Composable
fun ObtenerImagenUsuario(
    painter: MutableState<BitmapPainter>? = null,
    showBottomSheet: MutableState<Boolean>
) {
    if (painter != null) {
        Image(painter = painter.value, contentDescription = "", modifier = Modifier
            .size(150.dp)
            .fillMaxWidth(0.9f)
            .clip(RoundedCornerShape(16.dp))
            .clickable { showBottomSheet.value = true }
            .background(Color.Black), contentScale = ContentScale.FillBounds)
    }
}

fun transformar(
    imageUri: Uri?,
    imagenGaleria: MutableState<Bitmap>,
    content: Context,
    pintador: MutableState<BitmapPainter>
) {
    imageUri?.let {
        val bitmap = if (Build.VERSION.SDK_INT < 28) {
            MediaStore.Images.Media.getBitmap(content.contentResolver, it)
        } else {
            val source = ImageDecoder.createSource(content.contentResolver, it)
            ImageDecoder.decodeBitmap(source)
        }
        val rotatedBitmap = rotateBitmap(bitmap, 90F)
        val scaledBitmap = Bitmap.createScaledBitmap(rotatedBitmap, 400, 400, true)
        imagenGaleria.value = scaledBitmap
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

fun showCamera(barCodeLanzador: ManagedActivityResultLauncher<ScanOptions, ScanIntentResult>) {
    val options = ScanOptions()
    options.setDesiredBarcodeFormats(ScanOptions.ALL_CODE_TYPES)
    options.setPrompt("Escanea el codigo de barras")
    options.setCameraId(0)
    options.setBeepEnabled(false)
    options.setOrientationLocked(false)

    barCodeLanzador.launch(options)
}

fun checkCameraPermission(
    context: Context,
    barCodeLanzador: ManagedActivityResultLauncher<ScanOptions, ScanIntentResult>,
    requestPermissionLauncher: ManagedActivityResultLauncher<String, Boolean>
) {
    if (ContextCompat.checkSelfPermission(
            context,
            android.Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    ) {
        showCamera(barCodeLanzador)
    } else {
        requestPermissionLauncher.launch(android.Manifest.permission.CAMERA)
    }
}