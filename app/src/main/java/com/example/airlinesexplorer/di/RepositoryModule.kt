package com.example.airlinesexplorer.di

// RepositoryModule.kt


import com.example.airlinesexplorer.data.repo.AirlineRepositoryImpl
import com.example.airlinesexplorer.domain.AirlineRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAirlineRepository(
        airlineRepositoryImpl: AirlineRepositoryImpl
    ): AirlineRepository
}