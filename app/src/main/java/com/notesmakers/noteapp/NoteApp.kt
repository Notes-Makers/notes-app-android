package com.notesmakers.noteapp

import android.app.Application
import com.notesmakers.ai.di.AiModule
import com.notesmakers.database.di.DatabaseModule
import com.notesmakers.auth.di.AuthModule
import com.notesmakers.file.di.FileModule
import com.notesmakers.network.di.NetworkModule
import com.notesmakers.noteapp.di.AppModule
import org.koin.android.ext.koin.androidContext
import org.koin.ksp.generated.module

class NoteApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin()
    }

    private fun startKoin() {
        org.koin.core.context.startKoin {
            androidContext(this@NoteApp)
            modules(
                AppModule().module,
                AuthModule().module,
                FileModule().module,
                AiModule().module,
                DatabaseModule().module,
                NetworkModule().module,
            )
        }
    }
}