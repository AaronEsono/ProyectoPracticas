package com.example.practicaaaron.clases.basedatos.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.practicaaaron.clases.entidades.Usuario

@Dao
interface UsuarioDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(usuario: Usuario)

    @Query("SELECT * FROM USUARIO")
    fun getUsuario(): Usuario

    @Query("Select idUsuario FROM USUARIO")
    fun getIdUser():Int

    @Query("DELETE FROM USUARIO")
    fun borrarUsuario()

    @Query("Select tipoPerfil from USUARIO")
    fun getTipoPerfil():Int

}