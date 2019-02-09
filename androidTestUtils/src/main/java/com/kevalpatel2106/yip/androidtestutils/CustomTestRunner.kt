package com.kevalpatel2106.yip.androidtestutils

import android.annotation.SuppressLint
import android.app.KeyguardManager
import android.content.Context
import android.os.PowerManager
import androidx.test.runner.AndroidJUnitRunner


/**
 * Tests can fail for other reasons than code, it´ because of the animations and espresso device_sync and
 * emulator state (screen off or locked)
 *
 *
 * Before all the tests prepare the device to run tests and avoid these problems.
 *
 *
 * - Disable animations
 * - Disable keyguard lock
 * - Set it to be awake all the time (dont let the processor sleep)
 *
 * @see [https://github.com/JakeWharton/u2020](u2020 open source app by Jake Wharton)
 *
 * @see [https://gist.github.com/daj/7b48f1b8a92abf960e7b](Daj gist)
 */
class CustomTestRunner : AndroidJUnitRunner() {

    override fun onStart() {
        val context = this@CustomTestRunner.targetContext.applicationContext
        runOnMainSync {
            unlockScreen(context, CustomTestRunner::class.java.simpleName)
            keepScreenAwake(context, CustomTestRunner::class.java.simpleName)
        }
        super.onStart()
    }

    /**
     * Acquire the wakelock to keep the screen awake.
     *
     * @param context Instance of the app.
     * @param name    Name of the wakelock. (Tag)
     */
    @SuppressLint("WakelockTimeout")
    private fun keepScreenAwake(context: Context, name: String) {
        val power = context.getSystemService(Context.POWER_SERVICE) as PowerManager

        power.newWakeLock(PowerManager.FULL_WAKE_LOCK or PowerManager.ACQUIRE_CAUSES_WAKEUP
                or PowerManager.ON_AFTER_RELEASE, name)
                .acquire()
    }

    /**
     * Unlock the screen.
     *
     * @param context Instance of the app.
     * @param name    Name of the keyguard. (Tag)
     */
    private fun unlockScreen(context: Context, name: String) {
        val keyguard = context.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
        keyguard.newKeyguardLock(name).disableKeyguard()
    }
}