package com.notifier.app.auth.presentation.setup

import com.google.common.truth.Truth.assertThat
import com.notifier.app.auth.domain.AuthTokenDataSource
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

class SetupViewModelTest {
    private lateinit var authTokenDataSource: AuthTokenDataSource

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
                .map { it }
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
                .map { it }
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

            val stateStatuses = viewModel.state
                .take(2)
                .map { it }
                .toList()

            assertThat(stateStatuses).containsExactly(
                SetupState(SetupStep.FETCHING_TOKEN, authToken = null),
                SetupState(SetupStep.FAILED, authToken = null),
            ).inOrder()
        }
}
