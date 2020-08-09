package com.kevalpatel2106.yip.dashboard.inAppUpdate

import com.google.android.play.core.appupdate.AppUpdateInfo

sealed class InAppUpdateViewState {
    data class NotifyUpdateAvailable(val info: AppUpdateInfo) : InAppUpdateViewState()
    data class StartUpdateDownload(val info: AppUpdateInfo) : InAppUpdateViewState()
    object NotifyUpdateReadyToInstall : InAppUpdateViewState()
}
