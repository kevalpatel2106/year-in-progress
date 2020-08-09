package com.kevalpatel2106.yip.dashboard.inAppUpdate

import androidx.annotation.VisibleForTesting
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.install.InstallState
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.kevalpatel2106.yip.core.BaseViewModel
import com.kevalpatel2106.yip.core.livedata.SingleLiveEvent

internal class InAppUpdateViewModel @ViewModelInject constructor(
    private val updateManager: AppUpdateManager,
    private val helper: InAppUpdateHelper
) : BaseViewModel() {
    private val _inAppUpdateState = SingleLiveEvent<InAppUpdateViewState>()
    val inAppUpdateState: LiveData<InAppUpdateViewState> = _inAppUpdateState

    private val updateInstallListener = InstallStateUpdatedListener(::onUpdateDownloadStateChanged)

    init {
        // Start checking for update
        updateManager.appUpdateInfo.addOnSuccessListener(::onUpdateInfoReceived)
        updateManager.registerListener(updateInstallListener)
    }

    override fun onCleared() {
        super.onCleared()
        updateManager.unregisterListener(updateInstallListener)
    }

    fun onNewUpdateDownloadApproved(appUpdateInfoToDownload: AppUpdateInfo) {
        _inAppUpdateState.value = InAppUpdateViewState.StartUpdateDownload(appUpdateInfoToDownload)
    }

    fun onNewUpdateInstallApproved() {
        updateManager.completeUpdate()
    }

    // Need visible for testing because `FakeUpdateManage` does not work somehow
    @VisibleForTesting
    fun onUpdateInfoReceived(info: AppUpdateInfo) {
        if (helper.isUpdateDownloadable(info)) {
            helper.resetUpdateInfoAskedTime()
            _inAppUpdateState.value = InAppUpdateViewState.NotifyUpdateAvailable(info)
        }
    }

    // Need visible for testing because `FakeUpdateManage` does not work somehow
    @VisibleForTesting
    fun onUpdateDownloadStateChanged(state: InstallState) {
        if (helper.isUpdateDownloaded(state)) {
            _inAppUpdateState.value = InAppUpdateViewState.NotifyUpdateReadyToInstall
        }
    }
}
