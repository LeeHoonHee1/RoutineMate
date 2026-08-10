package com.example.routinemate.di

import com.example.routinemate.data.repository.AuthRepositoryImpl
import com.example.routinemate.domain.repository.AuthRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    // AuthRepository가 필요하면 AuthRepositoryImpl을 사용하도록 연결
    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        impl: AuthRepositoryImpl
    ): AuthRepository
}