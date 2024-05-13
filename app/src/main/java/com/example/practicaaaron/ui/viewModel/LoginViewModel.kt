package com.example.practicaaaron.ui.viewModel

import androidx.lifecycle.ViewModel
import com.example.practicaaaron.clases.basedatos.UsuarioRepositorio
import com.example.practicaaaron.clases.usuarios.Usuario

class LoginViewModel(private val usuarioRepositorio: UsuarioRepositorio):ViewModel(){

    suspend fun hola(){
        usuarioRepositorio.insertarUsuario(Usuario())
    }

    suspend fun adios(){
        usuarioRepositorio.cogerId()
    }

}