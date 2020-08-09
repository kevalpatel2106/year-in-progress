package com.kevalpatel2106.yip

import android.app.Application
import android.app.Instrumentation
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner

@Suppress("unused")
class UiTestsRunner : AndroidJUnitRunner() {
    override fun newApplication(cl: ClassLoader, className: String, context: Context): Application {
        // Use the test application class that can inject mock dependencies using dagger.
        return Instrumentation.newApplication(
            AndroidTestApplication_Application::class.java,
            context
        )
    }
}
