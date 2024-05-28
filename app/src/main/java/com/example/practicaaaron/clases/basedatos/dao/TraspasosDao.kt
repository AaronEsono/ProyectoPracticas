package com.example.practicaaaron.clases.basedatos.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.practicaaaron.clases.entidades.TransferirPedido

@Dao
interface TraspasosDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertar(transferirPedido: TransferirPedido)

    @Query("Select * from TRASPASOSINC")
    fun cogerTodos():List<TransferirPedido>

    @Query("Delete from TRASPASOSINC")
    fun borrarTodos()

}