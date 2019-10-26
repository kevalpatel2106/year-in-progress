package com.kevalpatel2106.yip.settings

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import com.kevalpatel2106.yip.R

internal object SettingsUseCase {

    internal fun prepareShareIntent(context: Context): Intent {
        return Intent().apply {
            action = Intent.ACTION_SEND
            type = "text/plain"
            addFlags(
                Intent.FLAG_ACTIVITY_CLEAR_TASK
                        or Intent.FLAG_ACTIVITY_CLEAR_TOP
                        or Intent.FLAG_ACTIVITY_NEW_TASK
            )
            putExtra(
                Intent.EXTRA_TEXT,
                context.getString(
                    R.string.app_invite_message,
                    context.getString(R.string.application_name),
                    context.getString(R.string.app_invitation_url)
                )
            )
            putExtra(
                Intent.EXTRA_SUBJECT,
                context.getString(
                    R.string.app_invitation_title,
                    context.getString(R.string.application_name)
                )
            )
        }
    }

    internal fun showLibraryLicences(context: Context) {
        OssLicensesMenuActivity.setActivityTitle(context.getString(R.string.title_activity_licences))
        context.startActivity(Intent(context, OssLicensesMenuActivity::class.java))
    }


    internal fun getNightModeSettings(context: Context, darkModeSettings: String?): Int {
        return when (darkModeSettings) {
            context.getString(R.string.dark_mode_on) -> AppCompatDelegate.MODE_NIGHT_YES
            context.getString(R.string.dark_mode_off) -> AppCompatDelegate.MODE_NIGHT_NO
            context.getString(R.string.dark_mode_system_default) -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            else -> AppCompatDelegate.MODE_NIGHT_NO
        }
    }
}
