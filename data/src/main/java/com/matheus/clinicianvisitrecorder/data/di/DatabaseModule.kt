package com.matheus.clinicianvisitrecorder.data.di

import android.content.Context
import androidx.room.Room
import com.matheus.clinicianvisitrecorder.data.database.AppDatabase
import com.matheus.clinicianvisitrecorder.data.model.VisitDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                "clinician_recorder.db"
            ).fallbackToDestructiveMigration(true).build()
    }

    @Provides
    fun provideVisitDao(db: AppDatabase): VisitDao = db.visitDao()
}