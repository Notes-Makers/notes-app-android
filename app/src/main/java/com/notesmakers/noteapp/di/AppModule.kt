package com.notesmakers.noteapp.di

import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
@ComponentScan("com.notesmakers.noteapp")
class AppModule {

    @Single
    fun provideString(): String = "DUPA"
}
