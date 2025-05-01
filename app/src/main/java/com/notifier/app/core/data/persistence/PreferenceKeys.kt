package com.notifier.app.core.data.persistence

import androidx.datastore.preferences.core.stringPreferencesKey

/**
 * Object containing preference keys used in DataStore.
 */
object PreferenceKeys {
    /** Key for storing the user's access token. */
    val ACCESS_TOKEN = stringPreferencesKey("access_token")
}
