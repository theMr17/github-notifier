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
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navDeepLink
import androidx.navigation.toRoute
import com.notifier.app.auth.presentation.login.LoginRoute
import com.notifier.app.auth.presentation.login.LoginScreen
import com.notifier.app.auth.presentation.setup.SetupRoute
import com.notifier.app.auth.presentation.setup.SetupScreen
import com.notifier.app.notification.presentation.NotificationRoute
import com.notifier.app.notification.presentation.NotificationScreen
import com.notifier.app.ui.theme.GitHubNotifierTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
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
                LoginRoute(
                    onNavigateToHomeScreen = {
                        navController.navigate(NotificationScreen) {
                            popUpTo(LoginScreen) {
                                inclusive = true
                            }
                        }
                    }
                )
            }

            composable<SetupScreen>(
                deepLinks = listOf(
                    navDeepLink<SetupScreen>(
                        basePath = "github-notifier://auth-callback"
                    )
                )
            ) {
                val args = it.toRoute<SetupScreen>()
                SetupRoute(code = args.code, onNavigateToHomeScreen = {
                    navController.navigate(NotificationScreen) {
                        popUpTo(LoginScreen) {
                            inclusive = true
                        }
                    }
                })
            }

            composable<NotificationScreen> {
                NotificationRoute()
            }
        }
    }
}
