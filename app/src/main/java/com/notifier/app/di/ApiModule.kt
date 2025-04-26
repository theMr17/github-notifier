package com.notifier.app.di

import com.notifier.app.core.data.networking.HttpClientFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {
    @Provides
    fun provideHttpClient(): HttpClient {
        return HttpClientFactory.create(CIO.create())
    }
}
