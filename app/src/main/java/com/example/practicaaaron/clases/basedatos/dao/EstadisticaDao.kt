package com.example.practicaaaron.clases.basedatos.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.practicaaaron.clases.entidades.EstadisticaUsuario

@Dao
interface EstadisticaDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertar(estadisticaUsuario: EstadisticaUsuario)

    @Query("Select * from Estadisticas where idUsuario = :id")
    fun encontrar(id:Int):EstadisticaUsuario

    @Query("DELETE FROM ESTADISTICAS")
    fun borrar()
}