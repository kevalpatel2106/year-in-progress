package com.kevalpatel2106.yip.dashboard.inAppUpdate

import androidx.activity.viewModels
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.google.android.material.snackbar.Snackbar
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.kevalpatel2106.yip.R
import com.kevalpatel2106.yip.core.ext.showSnack
import com.kevalpatel2106.yip.core.livedata.nullSafeObserve
import com.kevalpatel2106.yip.dashboard.inAppUpdate.InAppUpdateViewState.NotifyUpdateAvailable
import com.kevalpatel2106.yip.dashboard.inAppUpdate.InAppUpdateViewState.NotifyUpdateReadyToInstall
import com.kevalpatel2106.yip.dashboard.inAppUpdate.InAppUpdateViewState.StartUpdateDownload

internal class InAppUpdateManager(
    private val updateManager: AppUpdateManager,
    private val activity: FragmentActivity
) : LifecycleObserver {
    private val model: InAppUpdateViewModel by activity.viewModels()

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun observeViewModelState() {
        model.inAppUpdateState.nullSafeObserve(activity) { state ->
            when (state) {
                is NotifyUpdateAvailable -> askForDownloadApproval(state.info)
                is StartUpdateDownload -> updateManager.startUpdateFlowForResult(
                    state.info,
                    InAppUpdateHelperImpl.UPDATE_TYPE,
                    activity,
                    REQ_CODE
                )
                is NotifyUpdateReadyToInstall -> askForInstallApproval()
            }
        }
    }

    private fun askForDownloadApproval(info: AppUpdateInfo) {
        activity.showSnack(
            message = activity.getString(R.string.message_new_update_available),
            duration = Snackbar.LENGTH_LONG,
            actonTitle = R.string.download,
            actionListener = { model.onNewUpdateDownloadApproved(info) }
        )
    }

    private fun askForInstallApproval() {
        activity.showSnack(
            message = activity.getString(R.string.message_app_update_downloaded),
            duration = Snackbar.LENGTH_INDEFINITE,
            actonTitle = R.string.install_now,
            actionListener = { model.onNewUpdateInstallApproved() }
        )
    }

    companion object {
        private const val REQ_CODE = 3849
    }
}
