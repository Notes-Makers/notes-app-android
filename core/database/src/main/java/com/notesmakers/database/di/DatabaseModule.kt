package com.notesmakers.database.di

import com.notesmakers.database.data.entities.RealmBitmapDrawable
import com.notesmakers.database.data.entities.RealmNote
import com.notesmakers.database.data.entities.RealmPageOutput
import com.notesmakers.database.data.entities.RealmPathDrawable
import com.notesmakers.database.data.entities.RealmTextDrawable
import com.notesmakers.database.data.entities.RealmQuickNote
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
@ComponentScan("com.notesmakers.database")
class DatabaseModule {
    @Single
    fun providesRealmConfigs(): Realm {
        val config = RealmConfiguration.create(
            setOf(
                RealmNote::class,
                RealmPageOutput::class,
                RealmBitmapDrawable::class,
                RealmTextDrawable::class,
                RealmPathDrawable::class,
                RealmQuickNote::class,
            )
        )
        return Realm.open(config)
    }
}