package com.notifier.app.core.data.persistence

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import com.notifier.app.core.domain.util.Result
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class DataStoreManagerTest {
    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var dataStoreManager: DataStoreManager

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun testGetAccessToken_whenTokenExists_returnsToken() = runTest {
        val token = "persisted_token"
        // Save the token
        val result = dataStoreManager.setAccessToken(token)
        assertThat(result).isInstanceOf(Result.Success::class.java)

        // Retrieve the token
        val retrieved = dataStoreManager.getAccessToken()
        assertThat(retrieved).isInstanceOf(Result.Success::class.java)
        assertThat((retrieved as Result.Success).data).isEqualTo(token)
    }

    @Test
    fun testGetAccessToken_whenTokenNotSet_returnsEmptyString() = runTest {
        val result = dataStoreManager.getAccessToken()
        assertThat(result).isInstanceOf(Result.Success::class.java)
        assertThat((result as Result.Success).data).isEqualTo("")
    }

    @Test
    fun testSetAccessToken_withEmptyString_returnsEmptyString() = runTest {
        val result = dataStoreManager.setAccessToken("")
        assertThat(result).isInstanceOf(Result.Success::class.java)

        val token = dataStoreManager.getAccessToken()
        assertThat(token).isInstanceOf(Result.Success::class.java)
        assertThat((token as Result.Success).data).isEqualTo("")
    }

    @Test
    fun testSetAccessToken_overwritesExistingToken() = runTest {
        val initialToken = "initial_token"
        val updatedToken = "updated_token"

        // Set initial token
        var result = dataStoreManager.setAccessToken(initialToken)
        assertThat(result).isInstanceOf(Result.Success::class.java)

        // Set updated token
        result = dataStoreManager.setAccessToken(updatedToken)
        assertThat(result).isInstanceOf(Result.Success::class.java)

        // Retrieve the updated token
        val retrievedToken = dataStoreManager.getAccessToken()
        assertThat(retrievedToken).isInstanceOf(Result.Success::class.java)
        assertThat((retrievedToken as Result.Success).data).isEqualTo(updatedToken)
    }
}
