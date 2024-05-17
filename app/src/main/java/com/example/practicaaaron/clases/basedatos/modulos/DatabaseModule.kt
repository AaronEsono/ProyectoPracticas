package com.example.practicaaaron.clases.basedatos.modulos

import android.content.Context
import androidx.room.Room
import com.example.practicaaaron.clases.basedatos.bbdd.BaseDatos
import com.example.practicaaaron.clases.basedatos.dao.DataUsuarioDao
import com.example.practicaaaron.clases.basedatos.dao.EstadisticaDao
import com.example.practicaaaron.clases.basedatos.dao.PedidosDao
import com.example.practicaaaron.clases.basedatos.dao.UsuarioDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
 object DatabaseModule{


    @Provides
    @Singleton
    fun provideYourDatabase(
        context: Context
    ): BaseDatos {
        return synchronized(this){
            val instance = Room.databaseBuilder(
                context.applicationContext,
            BaseDatos::class.java,
            "baseDatos"
        ).fallbackToDestructiveMigration().build()
        instance
        }
    }

    @Provides
    @Singleton
    fun provideYourDao(db: BaseDatos): UsuarioDao = db.userDao()

    @Provides
    @Singleton
    fun provideYourDataUserDao(db: BaseDatos): DataUsuarioDao = db.DataUserDao()

    @Provides
    @Singleton
    fun provideYourEstadisticaDao(db: BaseDatos): EstadisticaDao = db.EstadisticaDao()

    @Provides
    @Singleton
    fun provideYourPedidoDao(db:BaseDatos): PedidosDao = db.PedidoDao()
}

