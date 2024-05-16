package com.example.practicaaaron.clases.basedatos.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.practicaaaron.clases.entidades.DataUsuario

@Dao
interface DataUsuarioDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertar(user: DataUsuario)

    @Query("Select * from UsuariosInfo")
    fun getAll():List<DataUsuario>

    @Query("Delete from UsuariosInfo")
    fun borrarTodos()
}