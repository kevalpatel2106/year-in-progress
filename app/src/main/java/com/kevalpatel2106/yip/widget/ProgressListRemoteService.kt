package com.kevalpatel2106.yip.widget

import android.content.Intent
import android.widget.RemoteViewsService
import com.kevalpatel2106.yip.di.getAppComponent
import javax.inject.Inject

class ProgressListRemoteService : RemoteViewsService() {

    @Inject
    internal lateinit var progressRemoteViewsFactory: ProgressListRemoteFactory

    override fun onCreate() {
        super.onCreate()
        getAppComponent().inject(this@ProgressListRemoteService)
    }

    override fun onGetViewFactory(intent: Intent?): RemoteViewsFactory {
        return progressRemoteViewsFactory
    }
}