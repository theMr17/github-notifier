package com.notifier.app.notification.data.networking

import com.notifier.app.core.data.networking.constructUrl
import com.notifier.app.core.data.networking.safeCall
import com.notifier.app.core.domain.util.NetworkError
import com.notifier.app.core.domain.util.Result
import com.notifier.app.core.domain.util.map
import com.notifier.app.notification.data.mappers.toNotification
import com.notifier.app.notification.data.networking.dto.NotificationResponseDto
import com.notifier.app.notification.domain.NotificationDataSource
import com.notifier.app.notification.domain.model.Notification
import io.ktor.client.HttpClient
import io.ktor.client.request.get

/**
 * A remote data source implementation for fetching notifications from a network API.
 *
 * This class communicates with the backend using Ktor's [HttpClient] to retrieve notification data.
 * It implements [NotificationDataSource] to ensure compatibility with different data sources.
 *
 * @property httpClient The HTTP client used for making network requests.
 */
class RemoteNotificationDataSource(
    private val httpClient: HttpClient,
) : NotificationDataSource {

    /**
     * Fetches notifications from the remote API.
     *
     * This function makes a GET request to the `/notification` endpoint, ensuring the URL is
     * properly constructed using [constructUrl]. The response is safely handled using [safeCall]
     * to catch errors such as network failures.
     *
     * @return A [Result] containing either a list of [Notification] objects on success, or a
     * [NetworkError] on failure.
     */
    override suspend fun getNotifications(): Result<List<Notification>, NetworkError> {
        return safeCall<NotificationResponseDto> {
            httpClient.get(
                urlString = constructUrl("/notification")
            )
        }.map { response ->
            response.data.map { it.toNotification() }
        }
    }
}
