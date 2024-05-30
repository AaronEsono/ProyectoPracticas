package com.example.practicaaaron.clases.basedatos.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.practicaaaron.clases.entidades.TransferirPedido

@Dao
interface TraspasosDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertar(transferirPedido: TransferirPedido)

    @Query("Select * from TRASPASOSINC where porEntregar = 1")
    fun cogerTodosSinActualizar():List<TransferirPedido>

    @Query("Select * from TRASPASOSINC where porEntregar = 0")
    fun pasarPedidos():List<TransferirPedido>

    @Query("Delete from TRASPASOSINC")
    fun borrarTodos()

    @Query("Delete from TRASPASOSINC where porEntregar = 0")
    fun borrarHechos()

    @Update
    fun actualizar(transferirPedido: TransferirPedido)
}