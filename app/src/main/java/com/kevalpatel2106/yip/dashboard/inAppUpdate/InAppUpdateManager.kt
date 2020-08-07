package com.kevalpatel2106.yip.dashboard.inAppUpdate

import android.app.Activity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.google.android.material.snackbar.Snackbar
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.install.InstallState
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.kevalpatel2106.yip.R
import com.kevalpatel2106.yip.core.showSnack
import com.kevalpatel2106.yip.dashboard.inAppUpdate.InAppUpdateHelper.Companion.UPDATE_TYPE

internal class InAppUpdateManager(
    private val updateManager: AppUpdateManager,
    private val inAppUpdateHelper: InAppUpdateHelper,
    private val activity: Activity
) : LifecycleObserver {
    private val updateInstallListener = InstallStateUpdatedListener(::onUpdateDownloadStateChanged)

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun checkForAppUpdate() {
        updateManager.appUpdateInfo.addOnSuccessListener(::onUpdateInfoReceived)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun registerAppUpdate() {
        updateManager.registerListener(updateInstallListener)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun unregisterAppUpdate() {
        updateManager.unregisterListener(updateInstallListener)
    }

    private fun onUpdateInfoReceived(info: AppUpdateInfo) {
        if (inAppUpdateHelper.isUpdateDownloadable(info)) {
            inAppUpdateHelper.resetUpdateInfoAskedTime()
            activity.showSnack(
                message = activity.getString(R.string.message_new_update_available),
                duration = Snackbar.LENGTH_LONG,
                actonTitle = R.string.download,
                actionListener = {
                    updateManager.startUpdateFlowForResult(info, UPDATE_TYPE, activity, REQ_CODE)
                }
            )
        }
    }

    private fun onUpdateDownloadStateChanged(state: InstallState) {
        if (inAppUpdateHelper.isUpdateDownloaded(state)) {
            activity.showSnack(
                message = activity.getString(R.string.message_app_update_downloaded),
                duration = Snackbar.LENGTH_INDEFINITE,
                actonTitle = R.string.install_now,
                actionListener = { updateManager.completeUpdate() }
            )
        }
    }

    companion object {
        private const val REQ_CODE = 3849
    }
}
