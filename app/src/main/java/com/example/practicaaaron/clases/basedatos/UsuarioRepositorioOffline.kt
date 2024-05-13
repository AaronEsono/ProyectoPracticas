package com.example.practicaaaron.clases.basedatos

import com.example.practicaaaron.clases.usuarios.Usuario
import kotlinx.coroutines.flow.Flow

class UsuarioRepositorioOffline(private val usuarioDao: UsuarioDao) : UsuarioRepositorio {
    override suspend fun insertarUsuario(usuario: Usuario) = usuarioDao.insert(usuario)

    override suspend fun cogerId(): Flow<Int> = usuarioDao.cogerId()

    override suspend fun pedirTodo(): Flow<Usuario> = usuarioDao.cogerTodo()
}