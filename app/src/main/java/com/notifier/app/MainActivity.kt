package com.notifier.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navDeepLink
import androidx.navigation.toRoute
import com.notifier.app.auth.presentation.connecting.ConnectingToGitHubRoute
import com.notifier.app.auth.presentation.connecting.ConnectingToGitHubScreen
import com.notifier.app.auth.presentation.login.LoginRoute
import com.notifier.app.auth.presentation.login.LoginScreen
import com.notifier.app.ui.theme.GitHubNotifierTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GitHubNotifierTheme {
                MainAppContent()
            }
        }
    }
}

@Composable
private fun MainAppContent() {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        val navController = rememberNavController()

        NavHost(
            navController = navController,
            startDestination = LoginScreen,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable<LoginScreen> {
                LoginRoute()
            }

            composable<ConnectingToGitHubScreen>(
                deepLinks = listOf(
                    navDeepLink<ConnectingToGitHubScreen>(
                        basePath = "github-notifier://auth-callback"
                    )
                )
            ) {
                val args = it.toRoute<ConnectingToGitHubScreen>()
                ConnectingToGitHubRoute(code = args.code)
            }
        }
    }
}
