package com.example.practicaaaron.clases.basedatos

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.practicaaaron.clases.usuarios.Usuario

@Database(entities = [Usuario::class], version = 1, exportSchema = false)
abstract class BaseDatos : RoomDatabase() {
    abstract fun userDao(): UsuarioDao

}