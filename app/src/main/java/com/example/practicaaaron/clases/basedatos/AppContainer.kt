package com.example.practicaaaron.clases.basedatos

import android.content.Context

interface AppContainer{
    val UsuarioRepositorio:UsuarioRepositorio
}

class AppDataContainer(private val context:Context) : AppContainer{

    override val UsuarioRepositorio : UsuarioRepositorio by lazy { 
        UsuarioRepositorioOffline(BaseDatos.getDatabase(context).userDao())
    }
    
    
}