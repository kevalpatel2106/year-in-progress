package com.kevalpatel2106.yip.dashboard.inAppUpdate

import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.install.InstallState

interface InAppUpdateHelper {
    fun isUpdateDownloadFinished(state: InstallState): Boolean
    fun isUpdateDownloadable(updateInfo: AppUpdateInfo): Boolean
    fun isUpdateAlreadyDownloaded(updateInfo: AppUpdateInfo): Boolean
}
