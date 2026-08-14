package com.example.routinemate.di

import com.example.routinemate.data.repository.AuthRepositoryImpl
import com.example.routinemate.data.repository.FriendRepositoryImpl
import com.example.routinemate.data.repository.HabitRepositoryImpl
import com.example.routinemate.data.repository.StatisticsRepositoryImpl
import com.example.routinemate.domain.repository.AuthRepository
import com.example.routinemate.domain.repository.FriendRepository
import com.example.routinemate.domain.repository.HabitRepository
import com.example.routinemate.domain.repository.StatisticsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    // 인증 Repository 바인딩
    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository

    // 습관 Repository 바인딩
    @Binds
    @Singleton
    abstract fun bindHabitRepository(
        habitRepositoryImpl: HabitRepositoryImpl
    ): HabitRepository

    // 통계 Repository 바인딩
    @Binds
    @Singleton
    abstract fun bindStatisticsRepository(
        statisticsRepositoryImpl: StatisticsRepositoryImpl
    ): StatisticsRepository

    // 친구 Repository 바인딩
    @Binds
    @Singleton
    abstract fun bindFriendRepository(
        friendRepositoryImpl: FriendRepositoryImpl
    ): FriendRepository
}