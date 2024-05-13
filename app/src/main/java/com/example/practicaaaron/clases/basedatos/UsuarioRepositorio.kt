package com.example.practicaaaron.clases.basedatos

import com.example.practicaaaron.clases.usuarios.Usuario
import kotlinx.coroutines.flow.Flow

interface UsuarioRepositorio {

    suspend fun insertarUsuario(usuario: Usuario)

    suspend fun cogerId(): Flow<Int>

    suspend fun pedirTodo(): Flow<Usuario>

}