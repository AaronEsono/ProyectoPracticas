package com.example.practicaaaron.clases.basedatos

import android.content.Context
import androidx.room.Room
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
    fun provideYourDao(db:BaseDatos):UsuarioDao = db.userDao()
}

