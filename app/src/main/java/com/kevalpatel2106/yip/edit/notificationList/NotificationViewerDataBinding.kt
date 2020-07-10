package com.kevalpatel2106.yip.edit.notificationList

import android.view.View
import androidx.databinding.BindingAdapter

@BindingAdapter("app:list")
fun list(view: View, list: List<Float>) {
    (view as? NotificationViewer)?.updateList(list)
}
