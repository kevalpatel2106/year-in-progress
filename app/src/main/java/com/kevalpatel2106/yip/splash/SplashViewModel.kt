package com.kevalpatel2106.yip.splash

import android.app.Application
import com.google.firebase.auth.FirebaseAuth
import com.kevalpatel2106.yip.BuildConfig
import com.kevalpatel2106.yip.core.BaseViewModel
import timber.log.Timber
import javax.inject.Inject

internal class SplashViewModel @Inject constructor(
    @Suppress("UNUSED_PARAMETER") application: Application
) : BaseViewModel() {
    private val firebaseAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    /**
     * Check if the devices has registered anonymous authentication.
     */
    private fun checkIsAuthenticated(): Boolean = firebaseAuth.currentUser != null || BuildConfig.DEBUG

    /**
     * Sign in anonymously in fire base if the device is not already registered.
     */
    internal fun signInAsAnonymousUser() {
        if (!checkIsAuthenticated()) {
            firebaseAuth.signInAnonymously().addOnCompleteListener {
                if (it.isSuccessful) {
                    Timber.d("Anonymous login successful. ID: ${it.result?.user?.uid}")
                } else {
                    Timber.e("Anonymous login failed.")
                }
            }
        }
    }
}