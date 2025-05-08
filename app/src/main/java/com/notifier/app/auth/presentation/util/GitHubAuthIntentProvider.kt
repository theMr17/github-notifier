package com.notifier.app.auth.presentation.util

import android.content.Intent
import androidx.core.net.toUri
import com.notifier.app.BuildConfig
import com.notifier.app.core.data.persistence.DataStoreManager
import kotlinx.coroutines.runBlocking
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

private const val GITHUB_AUTH_BASE_URL = "https://github.com/login/oauth/authorize"

@Singleton
class GitHubAuthIntentProvider @Inject constructor(
    private val dataStoreManager: DataStoreManager,
) {
    /**
     * Creates an [Intent] to launch the GitHub OAuth authorization page in a browser.
     *
     * @return [Intent] configured with the GitHub OAuth URL and required parameters.
     */
    fun createGitHubAuthIntent(): Pair<Intent, String> {
        val clientId = BuildConfig.CLIENT_ID
        val state = UUID.randomUUID().toString()

        runBlocking {
            dataStoreManager.setOAuthState(state)
        }

        val authUrl = "$GITHUB_AUTH_BASE_URL?client_id=$clientId&state=$state"

        return Pair(Intent(Intent.ACTION_VIEW, authUrl.toUri()), state)
    }
}
