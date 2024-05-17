package com.example.practicaaaron.clases.entidades.pedidos

import androidx.room.Embedded
import androidx.room.Relation
import com.example.practicaaaron.clases.entidades.Usuario

data class PedidoEntero(
    @Embedded
    var pedido:PCab = PCab(),

    @Relation(
        entity = PLin::class,
        parentColumn = "idPedido",
        entityColumn = "idPedido"
    )
    var bultos:List<PLin> = listOf(),

    @Relation(
        entity = Cliente::class,
        parentColumn = "idCliente",
        entityColumn = "idCliente"
    )
    val cliente: Cliente = Cliente(),

    @Relation(
        entity = Direccion::class,
        parentColumn = "idDireccion",
        entityColumn = "idDireccion"
    )
    val direccion: Direccion = Direccion(),

    @Relation(
        entity = Entrega::class,
        parentColumn = "idEntrega",
        entityColumn = "idEntrega"
    )
    val entrega: Entrega = Entrega()
)