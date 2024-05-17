package com.example.practicaaaron.clases.basedatos.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.practicaaaron.clases.entidades.pedidos.Cliente
import com.example.practicaaaron.clases.entidades.pedidos.Direccion
import com.example.practicaaaron.clases.entidades.pedidos.Entrega
import com.example.practicaaaron.clases.entidades.pedidos.PCab
import com.example.practicaaaron.clases.entidades.pedidos.PLin
import com.example.practicaaaron.clases.entidades.pedidos.PedidoEntero

@Dao
interface PedidosDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertarPedido(pCab: PCab)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertarEntrega(entrega: Entrega)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertarDireccion(direccion: Direccion)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertarCliente(cliente: Cliente)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertarBultos(bultos:PLin)

    @Query("SELECT * FROM PEDIDOS WHERE idUsuario = :id and date(fEntrega) = date(:fecha)")
    fun getPedidos(id:Int,fecha:String):List<PedidoEntero>

    @Query("Select * FROM PEDIDOS where idPedido = :id")
    fun getPedido(id:Int):PedidoEntero
}