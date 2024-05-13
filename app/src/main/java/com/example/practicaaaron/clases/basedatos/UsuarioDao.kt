package com.example.practicaaaron.clases.basedatos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.practicaaaron.clases.usuarios.Usuario
import kotlinx.coroutines.flow.Flow

@Dao
interface UsuarioDao {

    @Insert
    suspend fun insert(usuario:Usuario)

    @Query("select idUsuario from usuario limit 1")
    suspend fun cogerId(): Flow<Int>

     @Query("Select * from usuario")
     suspend fun cogerTodo(): Flow<Usuario>

}