package com.example.practicaaaron.clases.basedatos.bbdd

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.practicaaaron.clases.basedatos.dao.DataUsuarioDao
import com.example.practicaaaron.clases.basedatos.dao.EstadisticaDao
import com.example.practicaaaron.clases.basedatos.dao.PedidosDao
import com.example.practicaaaron.clases.basedatos.dao.TraspasosDao
import com.example.practicaaaron.clases.basedatos.dao.UsuarioDao
import com.example.practicaaaron.clases.entidades.DataUsuario
import com.example.practicaaaron.clases.entidades.EstadisticaUsuario
import com.example.practicaaaron.clases.entidades.TransferirPedido
import com.example.practicaaaron.clases.entidades.Usuario
import com.example.practicaaaron.clases.entidades.pedidos.Cliente
import com.example.practicaaaron.clases.entidades.pedidos.Direccion
import com.example.practicaaaron.clases.entidades.pedidos.Entrega
import com.example.practicaaaron.clases.entidades.pedidos.PCab
import com.example.practicaaaron.clases.entidades.pedidos.PLin

@Database(
    entities = [Usuario::class, DataUsuario::class, EstadisticaUsuario::class,Cliente::class,Direccion::class,Entrega::class,PCab::class,PLin::class,TransferirPedido::class],
    version = 13,
    exportSchema = false
)
abstract class BaseDatos : RoomDatabase() {
    abstract fun userDao(): UsuarioDao
    abstract fun DataUserDao(): DataUsuarioDao
    abstract fun EstadisticaDao(): EstadisticaDao
    abstract fun PedidoDao():PedidosDao
    abstract fun TraspasoDao():TraspasosDao
}