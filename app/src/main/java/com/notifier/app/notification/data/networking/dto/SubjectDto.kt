package com.notifier.app.notification.data.networking.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SubjectDto(
    @SerialName("latest_comment_url")
    val latestCommentUrl: String,
    @SerialName("title")
    val title: String,
    @SerialName("type")
    val type: String,
    @SerialName("url")
    val url: String,
)
