package com.notifier.app.core.presentation.util

import android.content.Context
import android.widget.Toast

/**
 * Extension function to show a Toast message in the given context.
 *
 * @param context the context in which the Toast should be shown.
 * @param message the message to be shown in the Toast.
 * @param duration the duration for which the Toast should be shown. Defaults to Toast.LENGTH_LONG.
 */
fun showToast(context: Context, message: String, duration: Int = Toast.LENGTH_LONG) {
    Toast.makeText(context, message, duration).show()
}
