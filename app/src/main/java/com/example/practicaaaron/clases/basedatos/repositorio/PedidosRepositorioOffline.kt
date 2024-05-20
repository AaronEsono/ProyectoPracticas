package com.example.practicaaaron.clases.basedatos.repositorio

import com.example.practicaaaron.clases.basedatos.dao.EstadisticaDao
import com.example.practicaaaron.clases.basedatos.dao.PedidosDao
import com.example.practicaaaron.clases.entidades.pedidos.Cliente
import com.example.practicaaaron.clases.entidades.pedidos.Direccion
import com.example.practicaaaron.clases.entidades.pedidos.Entrega
import com.example.practicaaaron.clases.entidades.pedidos.PCab
import com.example.practicaaaron.clases.entidades.pedidos.PLin
import javax.inject.Inject

class PedidosRepositorioOffline @Inject constructor(private val pedidosDao: PedidosDao){
    fun insertarPedido(pCab: PCab) = pedidosDao.insertarPedido(pCab)
    fun insertarBultos(bultos:PLin) = pedidosDao.insertarBultos(bultos)
    fun insertarDireccion(direccion: Direccion) = pedidosDao.insertarDireccion(direccion)
    fun insertarCliente(cliente: Cliente) = pedidosDao.insertarCliente(cliente)
    fun insertarEntrega(entrega: Entrega) = pedidosDao.insertarEntrega(entrega)

    fun getPedidosHoy(id:Int,fecha:String) = pedidosDao.getPedidos(id,fecha)

    fun getPedido(id:Int) = pedidosDao.getPedido(id)
    fun actualizarIncidencia(incidencia:Int,id:Int) = pedidosDao.updateIncidencia(incidencia, id)

    fun estanTodos(id:Int) = pedidosDao.todosEntregadosDia(id)
    fun darNombre(id:Int) = pedidosDao.darNombre(id)
    fun getIdEntrega(id:Int) = pedidosDao.getIdEntrega(id)
    fun updateEntrega(entrega: Entrega) = pedidosDao.updateEntrega(entrega)
    fun updatePedido(id:Int) = pedidosDao.actualizarPedido(id)
}