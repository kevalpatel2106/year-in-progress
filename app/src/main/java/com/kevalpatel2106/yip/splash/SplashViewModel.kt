package com.kevalpatel2106.yip.splash

import android.app.Activity
import com.google.firebase.auth.FirebaseAuth
import com.kevalpatel2106.feature.core.BuildConfig
import com.kevalpatel2106.yip.core.BaseViewModel
import com.kevalpatel2106.yip.repo.billing.BillingRepo
import timber.log.Timber
import javax.inject.Inject

internal class SplashViewModel @Inject constructor(
    private val billingRepo: BillingRepo
) : BaseViewModel() {
    private val firebaseAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    private fun checkIsAuthenticated(): Boolean =
        firebaseAuth.currentUser != null || BuildConfig.DEBUG

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

    internal fun refreshPurchaseState(activity: Activity) {
        billingRepo.refreshPurchaseState(activity)
    }
}