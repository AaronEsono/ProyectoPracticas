package com.example.practicaaaron.clases.entidades.pedidos

import androidx.room.Embedded
import androidx.room.Relation

data class RelPCabPLin (
    @Embedded val pedido:PCab,
    @Relation(
        parentColumn = "idPedido",
        entityColumn = "idPedido"
    )
    val pBultos: List<PLin>
)