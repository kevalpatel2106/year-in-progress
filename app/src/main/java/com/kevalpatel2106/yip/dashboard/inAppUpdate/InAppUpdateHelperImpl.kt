package com.kevalpatel2106.yip.dashboard.inAppUpdate

import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.install.InstallState
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import javax.inject.Inject

internal class InAppUpdateHelperImpl @Inject constructor() : InAppUpdateHelper {
    override fun isUpdateDownloadFinished(state: InstallState): Boolean {
        return state.installStatus() == InstallStatus.DOWNLOADED
    }

    override fun isUpdateDownloadable(updateInfo: AppUpdateInfo): Boolean {
        val stalenessDay = updateInfo.clientVersionStalenessDays()
        return updateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                && (stalenessDay == null || stalenessDay <= MAX_STALENESS_TIME)
    }

    override fun isUpdateAlreadyDownloaded(updateInfo: AppUpdateInfo): Boolean {
        return updateInfo.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS
    }

    companion object {
        const val UPDATE_TYPE = AppUpdateType.FLEXIBLE
        private const val MAX_STALENESS_TIME = 7
    }
}
