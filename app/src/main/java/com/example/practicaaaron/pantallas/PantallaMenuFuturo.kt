package com.example.practicaaaron.pantallas

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.practicaaaron.clases.utilidades.ManufacturedDate
import com.example.practicaaaron.ui.viewModel.MenuViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PantallaMenuFuturo(navHostController: NavHostController, menuViewModel: MenuViewModel = hiltViewModel()){
    ManufacturedDate(navHostController, menuViewModel)
}