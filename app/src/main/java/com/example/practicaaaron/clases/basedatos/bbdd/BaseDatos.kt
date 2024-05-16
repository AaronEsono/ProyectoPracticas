package com.example.practicaaaron.clases.basedatos.bbdd

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.practicaaaron.clases.basedatos.dao.DataUsuarioDao
import com.example.practicaaaron.clases.basedatos.dao.EstadisticaDao
import com.example.practicaaaron.clases.basedatos.dao.UsuarioDao
import com.example.practicaaaron.clases.entidades.DataUsuario
import com.example.practicaaaron.clases.entidades.EstadisticaUsuario
import com.example.practicaaaron.clases.entidades.Usuario

@Database(entities = [Usuario::class, DataUsuario::class,EstadisticaUsuario::class], version = 3, exportSchema = false)
abstract class BaseDatos : RoomDatabase() {
    abstract fun userDao(): UsuarioDao
    abstract fun DataUserDao(): DataUsuarioDao
    abstract fun EstadisticaDao(): EstadisticaDao
}