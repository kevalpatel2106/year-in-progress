package com.kevalpatel2106.yip.dashboard.inAppUpdate

import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.install.InstallState

interface InAppUpdateHelper {
    fun isUpdateDownloaded(state: InstallState): Boolean
    fun isUpdateDownloadable(updateInfo: AppUpdateInfo): Boolean
    fun resetUpdateInfoAskedTime()
}
