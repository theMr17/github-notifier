package com.notifier.app.notification.data.mappers

import com.notifier.app.notification.data.networking.dto.NotificationDto
import com.notifier.app.notification.data.networking.dto.OwnerDto
import com.notifier.app.notification.data.networking.dto.RepositoryDto
import com.notifier.app.notification.data.networking.dto.SubjectDto
import com.notifier.app.notification.domain.Notification
import com.notifier.app.notification.domain.Owner
import com.notifier.app.notification.domain.Repository
import com.notifier.app.notification.domain.Subject
import java.time.Instant
import java.time.ZoneId

fun NotificationDto.toNotification() = Notification(
    id = id,
    lastReadAt = Instant.parse(lastReadAt).atZone(ZoneId.systemDefault()),
    reason = reason,
    repository = repositoryDto.toRepository(),
    subject = subjectDto.toSubject(),
    subscriptionUrl = subscriptionUrl,
    unread = unread,
    updatedAt = Instant.parse(updatedAt).atZone(ZoneId.systemDefault()),
    url = url
)

fun RepositoryDto.toRepository() = Repository(
    fork = fork,
    forksUrl = forksUrl,
    fullName = fullName,
    htmlUrl = htmlUrl,
    id = id,
    name = name,
    nodeId = nodeId,
    notificationsUrl = notificationsUrl,
    owner = ownerDto.toOwner(),
    private = private
)

fun OwnerDto.toOwner() = Owner(
    avatarUrl = avatarUrl,
    eventsUrl = eventsUrl,
    id = id,
    login = login,
    nodeId = nodeId,
    type = type,
    url = url
)

fun SubjectDto.toSubject() = Subject(
    latestCommentUrl = latestCommentUrl,
    title = title,
    type = type,
    url = url
)
