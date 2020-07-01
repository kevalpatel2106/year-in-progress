package com.kevalpatel2106.yip.widget

import android.content.Intent
import android.widget.RemoteViewsService

import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ProgressListRemoteService : RemoteViewsService() {

    @Inject
    internal lateinit var progressRemoteViewsFactory: ProgressListRemoteFactory

    override fun onGetViewFactory(intent: Intent?): RemoteViewsFactory = progressRemoteViewsFactory
}
