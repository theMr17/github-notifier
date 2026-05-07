package com.notifier.app.core.data.persistence

import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey

/**
 * Object containing preference keys used in DataStore.
 */
object PreferenceKeys {
    /** Key for storing the user's access token. */
    val ACCESS_TOKEN = stringPreferencesKey("access_token")

    /** Key for storing the user's oauth state. */
    val OAUTH_STATE = stringPreferencesKey("oauth_state")

    /** Key for storing IDs of GitHub notifications that have already been shown as system notifications. */
    val SHOWN_NOTIFICATION_IDS = stringSetPreferencesKey("shown_notification_ids")
}
