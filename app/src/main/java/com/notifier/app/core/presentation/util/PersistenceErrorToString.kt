package com.notifier.app.core.presentation.util

import android.content.Context
import com.notifier.app.R
import com.notifier.app.core.domain.util.PersistenceError

fun PersistenceError.toString(context: Context): String {
    val resId = when (this) {
        PersistenceError.IO -> R.string.error_io
        PersistenceError.SERIALIZATION -> R.string.error_serialization
        PersistenceError.UNKNOWN -> R.string.error_unknown
    }
    return context.getString(resId)
}
