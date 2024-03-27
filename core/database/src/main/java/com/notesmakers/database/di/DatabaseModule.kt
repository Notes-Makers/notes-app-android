package com.notesmakers.database.di

import com.notesmakers.database.data.models.Note
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
@ComponentScan("com.notesmakers.database")
class DatabaseModule {
    @Single
    fun provideString(): String = "test iksde"
    @Single
    fun providesRealmConfigs(): Realm {
        val config = RealmConfiguration.create(setOf(Note::class))
        return Realm.open(config)
    }
}