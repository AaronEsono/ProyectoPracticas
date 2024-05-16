package com.example.practicaaaron.clases.basedatos.repositorio

import com.example.practicaaaron.clases.basedatos.dao.DataUsuarioDao
import com.example.practicaaaron.clases.entidades.DataUsuario
import javax.inject.Inject

class DataUsuarioRepositorioOffline @Inject constructor(private val dataUsuarioDao: DataUsuarioDao)  {
    fun insertar(user:DataUsuario) = dataUsuarioDao.insertar(user)
    fun obtenerTodos() = dataUsuarioDao.getAll()
    fun borrarTodos() = dataUsuarioDao.borrarTodos()
}