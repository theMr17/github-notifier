package com.notifier.app.auth.presentation.login

import com.google.common.truth.Truth.assertThat
import com.notifier.app.core.data.persistence.DataStoreManager
import com.notifier.app.core.domain.util.PersistenceError
import com.notifier.app.core.domain.util.Result
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class LoginViewModelTest {
    private lateinit var dataStoreManager: DataStoreManager

    private lateinit var viewModel: LoginViewModel

    @Before
    fun setUp() {
        dataStoreManager = mockk()
        viewModel = LoginViewModel(dataStoreManager)
    }

    @Test
    fun testAuthStatus_whenTokenIsValid_setsStateToLoggedInEventually() = runTest {
        coEvery {
            dataStoreManager.getAccessToken()
        } returns Result.Success("dummy_valid_token")

        val stateStatuses = viewModel.state
            .take(3)
            .map { it.status }
            .toList()

        assertThat(stateStatuses).containsExactly(
            null,
            LoginStatus.LOADING,
            LoginStatus.LOGGED_IN
        ).inOrder()
    }

    @Test
    fun testAuthStatus_whenTokenIsBlank_setsStateToLoggedOutEventually() = runTest {
        coEvery {
            dataStoreManager.getAccessToken()
        } returns Result.Success("")

        val stateStatuses = viewModel.state
            .take(3)
            .map { it.status }
            .toList()

        assertThat(stateStatuses).containsExactly(
            null,
            LoginStatus.LOADING,
            LoginStatus.LOGGED_OUT
        ).inOrder()
    }

    @Test
    fun testAuthStatus_whenErrorFetchingToken_setsStateToLoggedOutEventually() = runTest {
        coEvery {
            dataStoreManager.getAccessToken()
        } returns Result.Error(PersistenceError.IO)

        val stateStatuses = viewModel.state
            .take(3)
            .map { it.status }
            .toList()

        assertThat(stateStatuses).containsExactly(
            null,
            LoginStatus.LOADING,
            LoginStatus.LOGGED_OUT
        ).inOrder()
    }

    @Test
    fun testOnAction_loginButtonClick_emitsNavigateToGitHubAuthEvent() = runTest {
        coEvery {
            dataStoreManager.getAccessToken()
        } returns Result.Success("dummy_valid_token")

        viewModel.onAction(LoginAction.OnLoginButtonClick)

        val event = viewModel.events.first()
        assertThat(event).isEqualTo(LoginEvent.NavigateToGitHubAuth)
    }

    @Test
    fun testOnAction_whenUserLoggedIn_emitsNavigateToHomeScreenEvent() = runTest {
        coEvery {
            dataStoreManager.getAccessToken()
        } returns Result.Success("dummy_valid_token")

        viewModel.onAction(LoginAction.OnUserLoggedIn)

        val event = viewModel.events.first()
        assertThat(event).isEqualTo(LoginEvent.NavigateToHomeScreen)
    }
}
