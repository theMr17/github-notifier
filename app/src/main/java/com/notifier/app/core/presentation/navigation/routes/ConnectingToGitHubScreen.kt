package com.notifier.app.core.presentation.navigation.routes

import kotlinx.serialization.Serializable

@Serializable
data class ConnectingToGitHubScreen(
    val code: String? = null,
    val state: String? = null,
)
