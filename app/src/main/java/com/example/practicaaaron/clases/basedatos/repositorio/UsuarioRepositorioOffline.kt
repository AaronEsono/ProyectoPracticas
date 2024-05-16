package com.example.practicaaaron.clases.basedatos.repositorio

import com.example.practicaaaron.clases.basedatos.dao.UsuarioDao
import com.example.practicaaaron.clases.entidades.Usuario
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UsuarioRepositorioOffline @Inject constructor(private val usuarioDao: UsuarioDao) {
    fun insertarUsuario(usuario: Usuario) = usuarioDao.insert(usuario)
    fun getUsuario() = usuarioDao.getUsuario()
    fun getId() = usuarioDao.getIdUser()
    fun borrar() = usuarioDao.borrarUsuario()
    fun getTipoPerfil() = usuarioDao.getTipoPerfil()
}