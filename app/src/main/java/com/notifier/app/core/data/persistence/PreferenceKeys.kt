package com.notifier.app.core.data.persistence

import androidx.datastore.preferences.core.stringPreferencesKey

/**
 * Object containing preference keys used in DataStore.
 */
object PreferenceKeys {
    /** Key for storing the user's access token. */
    val ACCESS_TOKEN = stringPreferencesKey("access_token")

    /** Key for storing the user's oauth state. */
    val OAUTH_STATE = stringPreferencesKey("oauth_state")
}
