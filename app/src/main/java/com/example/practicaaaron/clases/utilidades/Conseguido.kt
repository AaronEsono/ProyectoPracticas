package com.example.practicaaaron.clases.utilidades

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import kotlinx.coroutines.Job
import java.time.LocalDate

@Composable
fun DialogoConseguido(
    texto: Int,
    textoTitulo:Int,
    navHostController: NavHostController,
    fecha: LocalDate,
    id: Int,
    todos:Int,
    entrega:Boolean,
    onDismiss: () -> Job
) {
    val openAlertDialog = remember { mutableStateOf(true) }

    when {
        openAlertDialog.value -> {
            AlertDialog(
                icon = {
                    Icon(Icons.Rounded.Check, contentDescription = "Example Icon")
                },
                title = {
                    Text(text = stringResource(id = textoTitulo))
                },
                text = {
                    Text(text = stringResource(id = texto))
                },
                onDismissRequest = {
                    openAlertDialog.value = false
                    onDismiss()
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            if(todos != 0){
                                navHostController.navigate("pedidos/${fecha}/${id}")
                            }else{
                                navHostController.navigate("hecho/${id}/${fecha}")
                            }
                            onDismiss()
                            openAlertDialog.value = false
                        }
                    ) {
                        Text("Volver a pedidos")
                    }
                },
                dismissButton = {
                    if(!entrega){
                        TextButton(
                            onClick = {
                                onDismiss()
                                openAlertDialog.value = false
                            }
                        ) {
                            Text("Quedarse")
                        }
                    }
                }
            )
        }
    }
}