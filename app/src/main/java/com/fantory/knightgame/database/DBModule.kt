package com.fantory.knightgame.database

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DBModule {

    @Singleton // Tell Dagger-Hilt to create a singleton accessible everywhere in ApplicationCompenent (i.e. everywhere in the application)
    @Provides
    fun provideYourDatabase(@ApplicationContext app: Context) = Room.databaseBuilder(
        app,
        AppDatabase::class.java,
        "knightdb")
        .fallbackToDestructiveMigration()
        .build() // The reason we can construct a database for the repo

    @Named("SolutionDao")
    @Singleton
    @Provides
    fun provideSolutionDao(db: AppDatabase) = db.solutionDao() // The reason we can implement a Dao for the database
}