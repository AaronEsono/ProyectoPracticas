package com.example.practicaaaron.clases.entidades.pedidos

import androidx.room.Embedded
import androidx.room.Relation

data class RelPCabEntrega(
    @Embedded var pedido:PCab,

    @Relation(
        parentColumn = "idEntrega",
        entityColumn = "idEntrega"
    )

    @Embedded var entrega:Entrega
)