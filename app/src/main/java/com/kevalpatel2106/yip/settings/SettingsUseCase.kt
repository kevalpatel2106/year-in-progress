package com.kevalpatel2106.yip.settings

import android.content.Context
import android.content.Intent
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import com.kevalpatel2106.yip.R

internal object SettingsUseCase {

    internal fun prepareShareIntent(context: Context?): Intent? {
        context?.let {
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
        return null
    }

    internal fun showLibraryLicences(ctx: Context?) {
        ctx?.let { context ->
            OssLicensesMenuActivity.setActivityTitle(context.getString(R.string.title_activity_licences))
            context.startActivity(Intent(context, OssLicensesMenuActivity::class.java))
        }
    }
}