package com.kevalpatel2106.yip.dashboard.inAppUpdate

import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.install.InstallState
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import com.kevalpatel2106.yip.repo.sharedPrefs.SharedPrefsProvider
import java.util.concurrent.TimeUnit
import javax.inject.Inject

internal class InAppUpdateHelper @Inject constructor(
    private val prefsProvider: SharedPrefsProvider
) {
    fun isUpdateDownloaded(state: InstallState): Boolean {
        return state.installStatus() == InstallStatus.DOWNLOADED
    }

    fun isUpdateDownloadable(updateInfo: AppUpdateInfo): Boolean {
        val lastAskedTime = prefsProvider.getLongFromPreference(PREF_KEY)
        return updateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                && updateInfo.isUpdateTypeAllowed(UPDATE_TYPE)
                && (lastAskedTime < 0 || lastAskedTime - System.currentTimeMillis() > DAY_MILLS)
    }

    fun resetUpdateInfoAskedTime() {
        prefsProvider.savePreferences(PREF_KEY, System.currentTimeMillis())
    }

    companion object {
        private val DAY_MILLS = TimeUnit.DAYS.toMillis(1)
        private const val PREF_KEY = "in_app_update_last_asked_key"
        const val UPDATE_TYPE = AppUpdateType.FLEXIBLE
    }
}
