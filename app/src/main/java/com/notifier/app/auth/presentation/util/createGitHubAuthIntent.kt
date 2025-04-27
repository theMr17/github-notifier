package com.notifier.app.auth.presentation.util

import android.content.Intent
import androidx.core.net.toUri
import com.notifier.app.BuildConfig
import java.util.UUID

private const val GITHUB_AUTH_BASE_URL = "https://github.com/login/oauth/authorize"

/**
 * Creates an [Intent] to launch the GitHub OAuth authorization page in a browser.
 *
 * @return [Intent] configured with the GitHub OAuth URL and required parameters.
 */
fun createGitHubAuthIntent(): Intent {
    val clientId = BuildConfig.CLIENT_ID
    val state = UUID.randomUUID().toString()

    val authUrl = "$GITHUB_AUTH_BASE_URL?client_id=$clientId&state=$state"

    return Intent(Intent.ACTION_VIEW, authUrl.toUri())
}
