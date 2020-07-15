package com.kevalpatel2106.yip.webviews

import androidx.annotation.StringRes
import com.kevalpatel2106.yip.R

enum class WebViewLaunchContent(@StringRes val title: Int, @StringRes val link: Int) {
    PRIVACY_POLICY(R.string.title_activity_privacy_policy, R.string.privacy_policy_url),
    CHANGELOG(R.string.title_activity_changelog, R.string.changelog_url),
    ADD_WIDGET(R.string.title_activity_widget_guide, R.string.add_widget_guide_url)

}
