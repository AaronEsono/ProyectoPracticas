package com.example.practicaaaron.clases.utilidades

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
    val textoMen = remember { mutableStateOf(if(id == -1)"Confirmar" else "Volver a pedidos") }

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
                    Column (modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center){
                        Text(text = stringResource(id = texto))
                    }
                },
                onDismissRequest = {
                    openAlertDialog.value = false
                    onDismiss()
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            if(id == -1){
                                navHostController.navigate("menu")
                            }
                            else if(todos != 0){
                                navHostController.navigate("pedidos/${fecha}/${id}")
                            }else{
                                navHostController.navigate("hecho/${id}/${fecha}")
                            }
                            onDismiss()
                            openAlertDialog.value = false
                        }
                    ) {
                        Text(textoMen.value)
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