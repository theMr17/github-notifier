package com.notifier.app.auth.presentation.setup

import com.google.common.truth.Truth.assertThat
import com.notifier.app.auth.data.networking.RemoteAuthTokenDataSource
import com.notifier.app.core.data.persistence.DataStoreManager
import com.notifier.app.core.domain.util.NetworkError
import com.notifier.app.core.domain.util.PersistenceError
import com.notifier.app.core.domain.util.Result
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SetupViewModelTest {
    private lateinit var authTokenDataSource: RemoteAuthTokenDataSource

    private lateinit var dataStoreManager: DataStoreManager

    private lateinit var viewModel: SetupViewModel

    @Before
    fun setUp() {
        authTokenDataSource = mockk()
        dataStoreManager = mockk()
        viewModel = SetupViewModel(authTokenDataSource, dataStoreManager)
    }

    @Test
    fun testSetupState_initialStateIsCorrect() = runTest {
        val initialState = viewModel.state.first()
        assertThat(initialState.setupStep).isEqualTo(SetupStep.FETCHING_TOKEN)
        assertThat(initialState.authToken).isNull()
    }

    @Test
    fun testGetAuthToken_codeIsNull_setsStateToFailed() = runTest {
        viewModel.getAuthToken(code = null, receivedState = "dummy_valid_state")

        val finalState = viewModel.state.first()
        assertThat(finalState).isEqualTo(
            SetupState(SetupStep.FAILED, authToken = null)
        )
    }

    @Test
    fun testGetAuthToken_codeIsBlank_setsStateToFailed() = runTest {
        viewModel.getAuthToken(code = "", receivedState = "dummy_valid_state")

        val finalState = viewModel.state.first()
        assertThat(finalState).isEqualTo(
            SetupState(SetupStep.FAILED, authToken = null)
        )
    }

    @Test
    fun testGetAuthToken_receivedOauthStateIsNull_setsStateToFailed() = runTest {
        viewModel.getAuthToken(code = "dummy_valid_code", receivedState = null)

        val finalState = viewModel.state.first()
        assertThat(finalState).isEqualTo(
            SetupState(SetupStep.FAILED, authToken = null)
        )
    }

    @Test
    fun testGetAuthToken_receivedOauthStateIsBlank_setsStateToFailed() = runTest {
        viewModel.getAuthToken(code = "dummy_valid_code", receivedState = "")

        val finalState = viewModel.state.first()
        assertThat(finalState).isEqualTo(
            SetupState(SetupStep.FAILED, authToken = null)
        )
    }

    @Test
    fun testGetAuthToken_receivedStateDoesNotMatchSavedOauthState_setsStateToFailedEventually() =
        runTest {
            coEvery {
                dataStoreManager.getOAuthState()
            } returns Result.Success("different_state")

            viewModel.getAuthToken(
                code = "dummy_valid_code",
                receivedState = "dummy_invalid_state"
            )

            val stateStatuses = viewModel.state
                .take(2)
                .toList()

            assertThat(stateStatuses).containsExactly(
                SetupState(SetupStep.FETCHING_TOKEN, authToken = null),
                SetupState(SetupStep.FAILED, authToken = null),
            ).inOrder()
        }

    @Test
    fun testGetAuthToken_getOauthStateFailed_setsStateToFailedEventually() =
        runTest {
            coEvery {
                dataStoreManager.getOAuthState()
            } returns Result.Error(PersistenceError.IO)

            viewModel.getAuthToken(
                code = "dummy_valid_code",
                receivedState = "dummy_valid_state"
            )

            val stateStatuses = viewModel.state
                .take(2)
                .toList()

            assertThat(stateStatuses).containsExactly(
                SetupState(SetupStep.FETCHING_TOKEN, authToken = null),
                SetupState(SetupStep.FAILED, authToken = null),
            ).inOrder()
        }

    @Test
    fun testGetAuthToken_clearOAuthStateFailed_setsStateToFailedEventually() =
        runTest {
            coEvery {
                dataStoreManager.getOAuthState()
            } returns Result.Success("dummy_valid_state")

            coEvery {
                dataStoreManager.clearOAuthState()
            } returns Result.Error(PersistenceError.IO)

            viewModel.getAuthToken(
                code = "dummy_valid_code",
                receivedState = "dummy_valid_state"
            )

            advanceUntilIdle()

            val stateStatuses = viewModel.state
                .take(2)
                .toList()

            assertThat(stateStatuses).containsExactly(
                SetupState(SetupStep.FETCHING_TOKEN, authToken = null),
                SetupState(SetupStep.FAILED, authToken = null),
            ).inOrder()
        }

    @Test
    fun testGetAuthToken_networkError_setsStateToFailedEventually_emitsNetworkErrorEvent() =
        runTest {
            coEvery {
                dataStoreManager.getOAuthState()
            } returns Result.Success("dummy_valid_state")

            coEvery {
                dataStoreManager.clearOAuthState()
            } returns Result.Success(Unit)

            coEvery {
                authTokenDataSource.getAuthToken(
                    clientId = any(),
                    clientSecret = any(),
                    code = "dummy_valid_code"
                )
            } returns Result.Error(NetworkError.SERVER_ERROR)

            val eventDeferred = async {
                viewModel.events.first()
            }

            viewModel.getAuthToken(
                code = "dummy_valid_code",
                receivedState = "dummy_valid_state"
            )

            val stateStatuses = viewModel.state.take(2).toList()

            assertThat(stateStatuses).containsExactly(
                SetupState(SetupStep.FETCHING_TOKEN, authToken = null),
                SetupState(SetupStep.FAILED, authToken = null)
            ).inOrder()

            val event = eventDeferred.await()

            assertThat(event).isEqualTo(
                SetupEvent.NetworkErrorEvent(NetworkError.SERVER_ERROR)
            )
        }

    @Test
    fun testOnAction_onContinueButtonClick_emitsNavigateToHomeScreenEvent() = runTest {
        viewModel.onAction(SetupAction.OnContinueButtonClick)

        val event = viewModel.events.first()
        assertThat(event).isEqualTo(SetupEvent.NavigateToHomeScreen)
    }
}
