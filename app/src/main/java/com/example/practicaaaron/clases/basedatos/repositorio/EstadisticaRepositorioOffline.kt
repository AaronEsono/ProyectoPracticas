package com.example.practicaaaron.clases.basedatos.repositorio

import com.example.practicaaaron.clases.basedatos.dao.EstadisticaDao
import com.example.practicaaaron.clases.entidades.EstadisticaUsuario
import javax.inject.Inject

class EstadisticaRepositorioOffline @Inject constructor(private val estadisticaDao: EstadisticaDao) {
    fun insertar(estadisticaUsuario: EstadisticaUsuario) = estadisticaDao.insertar(estadisticaUsuario)
    fun encontrar(id:Int):EstadisticaUsuario = estadisticaDao.encontrar(id)
}