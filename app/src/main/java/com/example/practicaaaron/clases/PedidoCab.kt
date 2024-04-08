package com.example.practicaaaron.clases

import java.time.LocalDate

data class PedidoCab(
    val idPedido:Int,
    val fechaEntrega:LocalDate,
    val fechaActualizacion:LocalDate,
    val cliente: Cliente,
    val direccion: Direccion,
    val incidencia:Int,
    val nombrePedido:String,
    val descripcion:String,
    val imagenDescripcion:Int,
    val entrega: Entrega,
    val bultos: List<PedidoLin>
)