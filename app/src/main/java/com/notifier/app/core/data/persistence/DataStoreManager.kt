package com.notifier.app.core.data.persistence

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.notifier.app.core.domain.util.Error
import com.notifier.app.core.domain.util.PersistenceError
import com.notifier.app.core.domain.util.Result
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Manages access to DataStore for reading and writing app preferences.
 */
@Singleton
class DataStoreManager @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) {

    /**
     * Retrieves the stored access token from DataStore.
     *
     * @return [Result.Success] with the token, or [Result.Error] with a [PersistenceError].
     */
    suspend fun getAccessToken(): Result<String, Error> = runDataStoreCatching {
        dataStore.data
            .map { preferences -> preferences[PreferenceKeys.ACCESS_TOKEN] ?: "" }
            .first()
    }

    /**
     * Saves the given access token to DataStore.
     *
     * @param accessToken the token to store.
     * @return [Result.Success] if stored successfully, or [Result.Error] with a [PersistenceError].
     */
    suspend fun setAccessToken(accessToken: String): Result<Unit, Error> = runDataStoreCatching {
        dataStore.edit { preferences ->
            preferences[PreferenceKeys.ACCESS_TOKEN] = accessToken
        }
    }
}
