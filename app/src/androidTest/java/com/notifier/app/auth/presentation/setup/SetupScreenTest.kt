package com.notifier.app.auth.presentation.setup

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.google.common.truth.Truth.assertThat
import org.junit.Rule
import org.junit.Test

class SetupScreenTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun testSetupScreen_whenFetchingToken_showsConnectingMessage() {
        composeRule.setContent {
            SetupScreen(
                state = SetupState(setupStep = SetupStep.FETCHING_TOKEN),
                onAction = {}
            )
        }

        composeRule
            .onNodeWithText("Connecting to GitHub...")
            .assertIsDisplayed()
    }

    @Test
    fun testSetupScreen_whenSavingToken_showsSavingMessage() {
        composeRule.setContent {
            SetupScreen(
                state = SetupState(setupStep = SetupStep.SAVING_TOKEN),
                onAction = {}
            )
        }

        composeRule
            .onNodeWithText("Saving user information...")
            .assertIsDisplayed()
    }

    @Test
    fun testSetupScreen_whenSuccess_showsSuccessMessageAndContinueButton_clickTriggersCallback() {
        var capturedAction: SetupAction? = null

        composeRule.setContent {
            SetupScreen(
                state = SetupState(setupStep = SetupStep.SUCCESS),
                onAction = {
                    capturedAction = it
                }
            )
        }

        composeRule
            .onNodeWithText("Connected successfully!")
            .assertIsDisplayed()

        composeRule
            .onNodeWithText("Continue")
            .assertIsDisplayed()
            .performClick()

        assertThat(capturedAction).isEqualTo(SetupAction.OnContinueButtonClick)
    }

    @Test
    fun testSetupScreen_whenFailed_showsErrorMessage() {
        composeRule.setContent {
            SetupScreen(
                state = SetupState(setupStep = SetupStep.FAILED),
                onAction = {}
            )
        }

        composeRule
            .onNodeWithText("Connection failed. Please try again.")
            .assertIsDisplayed()
    }
}
