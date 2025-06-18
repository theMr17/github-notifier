package com.notifier.app.core.presentation.notification

import androidx.compose.material3.Text
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.test.filters.SdkSuppress
import com.notifier.app.ui.theme.GitHubNotifierTheme
import org.junit.Rule
import org.junit.Test

@SdkSuppress(minSdkVersion = 33)
class WithNotificationPermissionTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun permissionNotGranted_shouldShowRationale_showsRationalePrompt() {
        composeTestRule.setContent {
            GitHubNotifierTheme {
                WithNotificationPermission(
                    content = { Text("Main Content") },
                    permissionState = FakeNotificationPermissionState(
                        isGranted = false,
                        shouldShowRationale = true
                    )
                )
            }
        }

        // Check that rationale message is shown
        composeTestRule
            .onNodeWithText(
                "To deliver GitHub notifications like pull requests, issues, and mentions, " +
                        "we need notification access. Please allow it."
            )
            .assertIsDisplayed()

        // Check that 'Allow' button is shown
        composeTestRule
            .onNodeWithText("Allow Notifications")
            .assertIsDisplayed()

        // Check main content is still shown
        composeTestRule
            .onNodeWithText("Main Content")
            .assertIsDisplayed()
    }

    @Test
    fun permissionNotGranted_shouldNotShowRationale_showsDeniedPrompt() {
        composeTestRule.setContent {
            GitHubNotifierTheme {
                WithNotificationPermission(
                    content = { Text("Main Content") },
                    permissionState = FakeNotificationPermissionState(
                        isGranted = false,
                        shouldShowRationale = false
                    )
                )
            }
        }

        // Check that denied message is shown
        composeTestRule
            .onNodeWithText(
                "Notification access is required to show updates from your GitHub activity. " +
                        "Enable it from system settings."
            )
            .assertIsDisplayed()

        // Check that 'Open Settings' button is shown
        composeTestRule
            .onNodeWithText("Open Settings")
            .assertIsDisplayed()

        // Check main content is still shown
        composeTestRule
            .onNodeWithText("Main Content")
            .assertIsDisplayed()
    }

    @Test
    fun permissionGranted_doesNotShowPrompt() {
        composeTestRule.setContent {
            GitHubNotifierTheme {
                WithNotificationPermission(
                    content = { Text("Main Content") },
                    permissionState = FakeNotificationPermissionState(
                        isGranted = true,
                        shouldShowRationale = false
                    )
                )
            }
        }

        // Should only show main content
        composeTestRule
            .onNodeWithText("Main Content")
            .assertIsDisplayed()

        composeTestRule
            .onAllNodesWithText("Allow Notifications")
            .assertCountEquals(0)

        composeTestRule
            .onAllNodesWithText("Open Settings")
            .assertCountEquals(0)
    }
}
