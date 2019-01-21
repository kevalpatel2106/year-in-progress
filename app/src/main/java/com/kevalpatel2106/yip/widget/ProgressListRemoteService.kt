package com.kevalpatel2106.yip.widget

import android.content.Intent
import android.widget.RemoteViewsService

class ProgressListRemoteService : RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent?): RemoteViewsFactory {
        return ProgressListRemoteFactory(this@ProgressListRemoteService.application)
    }
}