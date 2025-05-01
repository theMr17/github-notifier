package com.notifier.app.core.data.persistence

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Manages access to DataStore for reading and writing app preferences.
 *
 * @property dataStore the injected instance of DataStore for preferences.
 */
@Singleton
class DataStoreManager @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {

    /**
     * Retrieves the stored access token from DataStore.
     *
     * @return the access token if found, or an empty string if not set or error occurs.
     */
    suspend fun getAccessToken(): String {
        return try {
            dataStore.data
                .map { preferences -> preferences[PreferenceKeys.ACCESS_TOKEN] ?: "" }
                .first()
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }

    /**
     * Saves the given access token to DataStore.
     *
     * @param accessToken the token to store.
     */
    suspend fun setAccessToken(accessToken: String) {
        dataStore.edit { preferences ->
            preferences[PreferenceKeys.ACCESS_TOKEN] = accessToken
        }
    }
}
