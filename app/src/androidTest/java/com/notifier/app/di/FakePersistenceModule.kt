package com.notifier.app.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

// Test-specific datastore filename to avoid conflicts with production data
private const val TEST_DATASTORE_NAME = "test_user_preferences"

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [PersistenceModule::class]
)
object FakePersistenceModule {
    @Volatile
    private var testDataStoreInstance: DataStore<Preferences>? = null

    /**
     * Provides a singleton test DataStore instance for use in instrumented tests.
     * Uses a ReplaceFileCorruptionHandler to reset to empty preferences on corruption.
     */
    @Provides
    @Singleton
    fun provideTestDataStore(
        @ApplicationContext context: Context,
    ): DataStore<Preferences> {
        // Double-checked locking to ensure singleton instance in a thread-safe manner
        return testDataStoreInstance ?: synchronized(this) {
            testDataStoreInstance ?: PreferenceDataStoreFactory.create(
                corruptionHandler = ReplaceFileCorruptionHandler {
                    emptyPreferences()
                },
                scope = CoroutineScope(Dispatchers.IO),
                produceFile = {
                    context.preferencesDataStoreFile(TEST_DATASTORE_NAME)
                }
            ).also { testDataStoreInstance = it }
        }
    }
}
