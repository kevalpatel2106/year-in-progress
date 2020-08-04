package com.kevalpatel2106.yip.utils

import android.app.Activity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.google.android.material.snackbar.Snackbar
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.install.InstallState
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import com.kevalpatel2106.yip.R
import com.kevalpatel2106.yip.core.showSnack

internal class InAppUpdateHelper(
    private val appUpdateManager: AppUpdateManager,
    private val activity: Activity
) : LifecycleObserver {
    private val updateInstallListener = InstallStateUpdatedListener(::startInstallingUpdate)

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun startCheckingForUpdate() {
        appUpdateManager.appUpdateInfo.addOnSuccessListener(::startDownloadingUpdateIfAvailable)
        appUpdateManager.registerListener(updateInstallListener)
    }

    private fun startDownloadingUpdateIfAvailable(updateInfo: AppUpdateInfo) {
        if (updateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
            && updateInfo.isUpdateTypeAllowed(UPDATE_TYPE)
        ) {
            activity.showSnack(
                message = activity.getString(R.string.message_new_update_available),
                duration = Snackbar.LENGTH_LONG,
                actonTitle = R.string.download,
                actionListener = {
                    appUpdateManager.startUpdateFlowForResult(
                        updateInfo, UPDATE_TYPE, activity, REQ_CODE
                    )
                }
            )
        }
    }

    private fun startInstallingUpdate(state: InstallState) {
        if (state.installStatus() == InstallStatus.DOWNLOADED) {
            activity.showSnack(
                message = activity.getString(R.string.message_app_update_downloaded),
                duration = Snackbar.LENGTH_INDEFINITE,
                actonTitle = R.string.install_now,
                actionListener = { appUpdateManager.completeUpdate() }
            )
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun unregisterAppUpdate() {
        appUpdateManager.unregisterListener(updateInstallListener)
    }

    companion object {
        private const val UPDATE_TYPE = AppUpdateType.FLEXIBLE
        private const val REQ_CODE = 3849
    }
}
