package com.notifier.app.notification.domain.model

data class Subject(
    val latestCommentUrl: String,
    val title: String,
    val type: String,
    val url: String,
)
