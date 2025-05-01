package com.notifier.app.core.domain

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner
import dagger.hilt.android.testing.HiltTestApplication

/**
 * Custom JUnit runner required for using Hilt in Android instrumented tests.
 * This replaces the default application class with [HiltTestApplication].
 */
class CustomTestRunner : AndroidJUnitRunner() {
    override fun newApplication(
        cl: ClassLoader?,
        name: String?,
        context: Context?
    ): Application {
        // Use HiltTestApplication as the application class for testing
        val applicationClassName = HiltTestApplication::class.java.name
        return super.newApplication(cl, applicationClassName, requireNotNull(context))
    }
}
