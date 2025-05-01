package com.notifier.app.core.data.persistence

import androidx.test.ext.junit.runners.AndroidJUnit4
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
        dataStoreManager.setAccessToken(token)

        val retrieved = dataStoreManager.getAccessToken()
        assertEquals(token, retrieved)
    }

    @Test
    fun testGetAccessToken_whenTokenNotSet_returnsEmptyString() = runTest {
        val token = dataStoreManager.getAccessToken()
        assertEquals("", token)
    }

    @Test
    fun testSetAccessToken_withEmptyString_returnsEmptyString() = runTest {
        dataStoreManager.setAccessToken("")
        val token = dataStoreManager.getAccessToken()
        assertEquals("", token)
    }

    @Test
    fun testSetAccessToken_overwritesExistingToken() = runTest {
        val initialToken = "initial_token"
        val updatedToken = "updated_token"

        dataStoreManager.setAccessToken(initialToken)
        dataStoreManager.setAccessToken(updatedToken)

        val retrievedToken = dataStoreManager.getAccessToken()
        assertEquals(updatedToken, retrievedToken)
    }
}
