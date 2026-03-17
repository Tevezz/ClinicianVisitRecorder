package com.matheus.clinicianvisitrecorder.data.di

import com.matheus.clinicianvisitrecorder.data.repository.PatientRepositoryImpl
import com.matheus.clinicianvisitrecorder.domain.repository.PatientRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class DataModule {

    @Binds
    internal abstract fun bindPatientRepository(
        impl: PatientRepositoryImpl
    ): PatientRepository
}