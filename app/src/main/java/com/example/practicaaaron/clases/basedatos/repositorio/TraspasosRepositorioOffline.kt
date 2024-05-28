package com.example.practicaaaron.clases.basedatos.repositorio

import com.example.practicaaaron.clases.basedatos.dao.TraspasosDao
import com.example.practicaaaron.clases.entidades.TransferirPedido
import javax.inject.Inject

class TraspasosRepositorioOffline @Inject constructor(private val traspasosDao: TraspasosDao){
    fun insertar(transferirPedido: TransferirPedido) = traspasosDao.insertar(transferirPedido)
    fun cogerTodos() = traspasosDao.cogerTodos()
    fun borrarTodos() = traspasosDao.borrarTodos()
}