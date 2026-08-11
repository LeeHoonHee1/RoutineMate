package com.example.routinemate.di

import com.example.routinemate.data.remote.api.AuthApi
import com.example.routinemate.data.remote.api.HabitApi
import com.example.routinemate.data.remote.api.TokenApi
import com.example.routinemate.data.remote.auth.TokenAuthenticator
import com.example.routinemate.data.remote.interceptor.AuthInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val BASE_URL = "http://10.0.2.2:8080/"

    @Provides
    @Singleton
    fun provideJson(): Json {
        return Json {
            ignoreUnknownKeys = true
        }
    }

    // 일반 API용 OkHttpClient
    @Provides
    @Singleton
    @Named("authOkHttpClient")
    fun provideAuthOkHttpClient(
        authInterceptor: AuthInterceptor,
        tokenAuthenticator: TokenAuthenticator
    ): OkHttpClient {

        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .authenticator(tokenAuthenticator)
            .build()
    }

    // Refresh Token 재발급 전용 OkHttpClient
    @Provides
    @Singleton
    @Named("tokenOkHttpClient")
    fun provideTokenOkHttpClient(): OkHttpClient {

        // Authenticator를 넣지 않음
        return OkHttpClient.Builder()
            .build()
    }

    // 일반 API용 Retrofit
    @Provides
    @Singleton
    @Named("authRetrofit")
    fun provideAuthRetrofit(
        json: Json,
        @Named("authOkHttpClient")
        okHttpClient: OkHttpClient
    ): Retrofit {

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(
                json.asConverterFactory(
                    "application/json".toMediaType()
                )
            )
            .build()
    }

    // Refresh Token 재발급 전용 Retrofit
    @Provides
    @Singleton
    @Named("tokenRetrofit")
    fun provideTokenRetrofit(
        json: Json,
        @Named("tokenOkHttpClient")
        okHttpClient: OkHttpClient
    ): Retrofit {

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(
                json.asConverterFactory(
                    "application/json".toMediaType()
                )
            )
            .build()
    }

    // 일반 인증 API
    @Provides
    @Singleton
    fun provideAuthApi(
        @Named("authRetrofit")
        retrofit: Retrofit
    ): AuthApi {

        return retrofit.create(AuthApi::class.java)
    }

    // Refresh Token 전용 API
    @Provides
    @Singleton
    fun provideTokenApi(
        @Named("tokenRetrofit")
        retrofit: Retrofit
    ): TokenApi {

        return retrofit.create(TokenApi::class.java)
    }

    // Habit API 제공
    @Provides
    @Singleton
    fun provideHabitApi(
        @Named("authRetrofit") retrofit: Retrofit
    ): HabitApi {

        return retrofit.create(HabitApi::class.java)
    }
}