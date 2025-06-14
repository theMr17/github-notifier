package com.notifier.app.di

import com.notifier.app.auth.data.networking.RemoteAuthTokenDataSource
import com.notifier.app.auth.domain.AuthTokenDataSource
import com.notifier.app.core.data.networking.HttpClientFactory
import com.notifier.app.notification.data.networking.RemoteNotificationDataSource
import com.notifier.app.notification.domain.NotificationDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {
    @Provides
    @Singleton
    fun provideHttpClient(
        httpClientFactory: HttpClientFactory,
    ): HttpClient {
        return httpClientFactory.create(CIO.create())
    }

    @Provides
    @Singleton
    fun provideRemoteAuthTokenDataSource(
        httpClient: HttpClient,
    ): AuthTokenDataSource {
        return RemoteAuthTokenDataSource(httpClient)
    }

    @Provides
    @Singleton
    fun provideRemoteNotificationDataSource(
        httpClient: HttpClient,
    ): NotificationDataSource {
        return RemoteNotificationDataSource(httpClient)
    }
}
