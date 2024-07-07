package com.mystic.nothanks.data.database.di

import android.content.Context
import androidx.room.Room
import com.mystic.nothanks.data.database.BoycottDatabase
import com.mystic.nothanks.data.database.dao.BoycottDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DbModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): BoycottDatabase = Room.databaseBuilder(
        context = context,
        klass = BoycottDatabase::class.java,
        name = "boycott_db",
    ).createFromAsset("dataset/dataset.db")
        .fallbackToDestructiveMigration()
        .build()

    @Provides
    @Singleton
    fun provideDao(
        database: BoycottDatabase
    ) : BoycottDao = database.databaseDao()


}