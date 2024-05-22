package com.example.practicaaaron.clases.utilidades

import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.practicaaaron.ui.theme.colorBoton
import com.example.practicaaaron.ui.theme.colorError
import kotlinx.coroutines.Job

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MensajeError(texto:Int,onDismiss: () -> Job){
    val modalBottomSheetState = rememberModalBottomSheetState()
    val textoMensaje = if(texto == -1) "Algo ha fallado." else stringResource(id = texto)

    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        sheetState = modalBottomSheetState,
        dragHandle = { BottomSheetDefaults.DragHandle() },
    ) {
        Box(
            modifier = Modifier
                .height(150.dp)
                .fillMaxWidth()
                .padding(10.dp, 0.dp)
        ) {
            Column (modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally){
                Text(text = textoMensaje,maxLines = 1, modifier = Modifier.basicMarquee(), fontWeight = FontWeight.Black)
                Spacer(modifier = Modifier.padding(10.dp))
                Button(onClick = { onDismiss() }, colors = ButtonDefaults.buttonColors(
                    containerColor = colorError
                ), shape = RoundedCornerShape(4.dp)
                ) {
                    Text(text = "Aceptar")
                }
            }
        }
    }
}