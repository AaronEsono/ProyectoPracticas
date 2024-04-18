package com.example.practicaaaron.clases.utilidades

import android.Manifest
import android.content.Context
import android.content.ContextWrapper
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.dynamsoft.dce.CameraEnhancer
import com.dynamsoft.dce.DCECameraView

@Composable
fun abrirCamara(){
     lateinit var mCameraEnhancer: CameraEnhancer

    AndroidView(factory = {context ->
        mCameraEnhancer = CameraEnhancer(context.findActivity())

        val mCameraView: DCECameraView = DCECameraView(context)

        mCameraView.overlayVisible = true
        mCameraEnhancer.cameraView = mCameraView
        mCameraView
    })

    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted ->
            if (granted == true) {
                mCameraEnhancer.open()
            }
        }
    )

    LaunchedEffect(key1 = true){
        launcher.launch(Manifest.permission.CAMERA)
    }

}

fun Context.findActivity(): ComponentActivity? = when (this) {
    is ComponentActivity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}