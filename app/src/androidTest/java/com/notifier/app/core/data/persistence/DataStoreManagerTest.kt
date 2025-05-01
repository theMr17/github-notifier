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
    fun getAccessToken_whenTokenExists_returnsToken() = runTest {
        val token = "persisted_token"
        dataStoreManager.setAccessToken(token)

        val retrieved = dataStoreManager.getAccessToken()
        assertEquals(token, retrieved)
    }

    @Test
    fun getAccessToken_whenTokenNotSet_returnsEmptyString() = runTest {
        val token = dataStoreManager.getAccessToken()
        assertEquals("", token)
    }
}
