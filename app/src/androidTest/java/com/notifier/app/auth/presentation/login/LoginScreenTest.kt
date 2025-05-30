package com.notifier.app.auth.presentation.login

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.google.common.truth.Truth.assertThat
import org.junit.Rule
import org.junit.Test

class LoginScreenTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun testLoginScreen_whenLoading_showsProgressAndMessage() {
        composeRule.setContent {
            LoginScreen(
                state = LoginState(status = LoginStatus.LOADING),
                onLoginButtonClick = {}
            )
        }

        composeRule
            .onNode(hasTestTag(TEST_TAG_CIRCULAR_PROGRESS_INDICATOR))
            .assertExists()
        composeRule
            .onNodeWithText("Verifying authentication status...")
            .assertIsDisplayed()
    }

    @Test
    fun testLoginScreen_whenLoggedOut_showsLoginButton_clickTriggersCallback() {
        var loginButtonClicked = false

        composeRule.setContent {
            LoginScreen(
                state = LoginState(status = LoginStatus.LOGGED_OUT),
                onLoginButtonClick = { loginButtonClicked = true }
            )
        }

        composeRule
            .onNodeWithText("Login with GitHub")
            .assertIsDisplayed()
            .performClick()

        assertThat(loginButtonClicked).isTrue()
    }

    @Test
    fun testLoginScreen_whenLoggedIn_showsSuccessMessage() {
        composeRule.setContent {
            LoginScreen(
                state = LoginState(status = LoginStatus.LOGGED_IN),
                onLoginButtonClick = {}
            )
        }

        composeRule
            .onNodeWithText("Logged in successfully! Redirecting...")
            .assertIsDisplayed()
    }
}
