package com.notifier.app.core.data.persistence

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.notifier.app.core.domain.util.Result
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
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
        assert(result is Result.Success)

        // Retrieve the token
        val retrieved = dataStoreManager.getAccessToken()
        assert(retrieved is Result.Success)
        assertEquals(token, (retrieved as Result.Success).data)
    }

    @Test
    fun testGetAccessToken_whenTokenNotSet_returnsEmptyString() = runTest {
        val result = dataStoreManager.getAccessToken()
        assert(result is Result.Success)
        assertEquals("", (result as Result.Success).data)
    }

    @Test
    fun testSetAccessToken_withEmptyString_returnsEmptyString() = runTest {
        val result = dataStoreManager.setAccessToken("")
        assert(result is Result.Success)

        val token = dataStoreManager.getAccessToken()
        assert(token is Result.Success)
        assertEquals("", (token as Result.Success).data)
    }

    @Test
    fun testSetAccessToken_overwritesExistingToken() = runTest {
        val initialToken = "initial_token"
        val updatedToken = "updated_token"

        // Set initial token
        var result = dataStoreManager.setAccessToken(initialToken)
        assert(result is Result.Success)

        // Set updated token
        result = dataStoreManager.setAccessToken(updatedToken)
        assert(result is Result.Success)

        // Retrieve the updated token
        val retrievedToken = dataStoreManager.getAccessToken()
        assert(retrievedToken is Result.Success)
        assertEquals(updatedToken, (retrievedToken as Result.Success).data)
    }
}
