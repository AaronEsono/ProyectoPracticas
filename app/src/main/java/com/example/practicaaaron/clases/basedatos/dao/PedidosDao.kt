package com.example.practicaaaron.clases.basedatos.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
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

    @Query("SELECT * FROM PEDIDOS WHERE idUsuario = :id and date(fEntrega) = date(:fecha) and incidencia != 100")
    fun getPedidosNoHechos(id:Int,fecha:String):List<PedidoEntero>

    @Query("Select * FROM PEDIDOS where idPedido = :id")
    fun getPedido(id:Int):PedidoEntero

    @Query("UPDATE PEDIDOS set incidencia = :incidencia, porEntregar = 0 where idPedido = :id")
    fun updateIncidencia(incidencia:Int,id:Int)

    @Query("UPDATE PEDIDOS set incidencia = :incidencia, porEntregar = 1 where idPedido = :id")
    fun updateIncidenciaOffline(incidencia:Int,id:Int)

    @Query("SELECT ((SELECT COUNT(*) FROM PEDIDOS where idUsuario = :id and date(fEntrega) = date('now')) - (SELECT COUNT(*) FROM PEDIDOS WHERE incidencia != 0 and idUsuario = :id and date(fEntrega) = date('now'))) AS resultado from pedidos")
    fun todosEntregadosDia(id:Int):Int

    @Query("Select nombre from PEDIDOS where idPedido = :id")
    fun darNombre(id:Int):String

    @Query("SELECT idEntrega from PEDIDOS where idPedido = :id")
    fun getIdEntrega(id:Int):Int

    @Update
    fun updateEntrega(entrega: Entrega)

    @Query("UPDATE PEDIDOS set estado = 1, incidencia = 100, porEntregar = 0 where idPedido = :id")
    fun actualizarPedido(id:Int)

    @Query("UPDATE PEDIDOS set estado = 1, incidencia = 100, porEntregar = 1 where idPedido = :id")
    fun actualizarPedidoOffline(id:Int)

    @Query("DELETE FROM PEDIDOS")
    fun borrarPedidos()

    @Query("DELETE FROM BULTOS")
    fun borrarBultos()

    @Query("DELETE FROM CLIENTES")
    fun borrarClientes()

    @Query("DELETE FROM DIRECCIONES")
    fun borrarDirecciones()

    @Query("DELETE FROM ENTREGAS")
    fun borrarEntregas()

    @Query("SELECT E.* FROM PEDIDOS P INNER JOIN ENTREGAS E ON E.idEntrega = P.idEntrega WHERE porEntregar = 1")
    fun devolverEnLocal():List<Entrega>

    @Query("SELECT P.idPedido FROM PEDIDOS P WHERE P.idEntrega = :idEntrega")
    fun devolverId(idEntrega:Int):Int

    @Query("SELECT * from pedidos WHERE porEntregar = 1 and idUsuario = :id")
    fun incidenciasPendientes(id:Int):List<PCab>

    @Query("UPDATE PEDIDOS set idUsuario=:idEntrega where idPedido = :idPedido and idUsuario=:idUsuario")
    fun transferirPedidos(idUsuario:Int,idPedido:Int,idEntrega: Int)

}