package com.example.practicaaaron.clases.basedatos.modulos

import com.example.practicaaaron.ui.viewModel.EventosViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ViewModelModule {

    @Provides
    @Singleton
    fun provideMainViewModel(): EventosViewModel {
        return EventosViewModel()
    }
}