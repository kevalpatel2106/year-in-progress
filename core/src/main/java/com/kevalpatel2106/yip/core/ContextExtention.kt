/*
 * Copyright (c) 2018. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.kevalpatel2106.yip.core

import android.app.Activity
import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.kevalpatel2106.feature.core.BuildConfig
import com.kevalpatel2106.feature.core.R
import timber.log.Timber

/**
 * Get the color from color res.
 */
fun Context.getColorCompat(@ColorRes color: Int) = ContextCompat.getColor(this, color)

fun <T : AppCompatActivity> Context.prepareLaunchIntent(
        aClass: Class<T>,
        isNewTask: Boolean = false
): Intent {

    return Intent(this, aClass).apply {
        if (isNewTask) {
            addFlags(
                    Intent.FLAG_ACTIVITY_CLEAR_TASK
                            or Intent.FLAG_ACTIVITY_CLEAR_TOP
                            or Intent.FLAG_ACTIVITY_NEW_TASK
            )
        }
    }
}

/**
 * Display the snack bar.
 */
fun Activity.showSnack(
        message: String,
        duration: Int = 2000,
        actonTitle: Int = -1,
        actionListener: ((View) -> Unit)? = null
): Snackbar {
    val snackbar = Snackbar.make(
            findViewById<ViewGroup>(android.R.id.content).getChildAt(0) as ViewGroup,
            message,
            duration
    ).apply {
        actonTitle.takeIf { it > 0 }?.let { actionTitle ->
            setAction(actionTitle, actionListener)
            setActionTextColor(getColorCompat(R.color.colorPrimary))
        }

        view.setBackgroundColor(getColorCompat(R.color.colorAccent))
        view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text).apply {
            setTextColor(getColorCompat(android.R.color.white))
            maxLines = 3
        }
    }
    snackbar.show()
    return snackbar
}

/**
 * Send an email to the development team.
 */
fun Context.sendMailToDev() {
    val emailText = ("<ENTER YOUR MESSAGE HERE>\n\n\n\n--------------------------\n"
            + "PLEASE DON'T REMOVE/EDIT BELOW INFO:\n"
            + "\nDEVICE NAME : " + Build.MODEL + "(" + Build.DEVICE + ")"
            + "\nMANUFACTURER : " + Build.MANUFACTURER
            + "\nANDROID VERSION : " + Build.VERSION.SDK_INT
            + "\nAPPLICATION VERSION : " + BuildConfig.VERSION_NAME + "(" + BuildConfig.VERSION_CODE + ")"
            + "\n--------------------------\n")


    val emailIntent = Intent(Intent.ACTION_SENDTO)
    emailIntent.data = Uri.parse(getString(R.string.email_scheme))
    emailIntent.putExtra(Intent.EXTRA_EMAIL, resources.getStringArray(R.array.support_email))
    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Query regarding ${BuildConfig.APPLICATION_NAME}")
    emailIntent.putExtra(Intent.EXTRA_TEXT, emailText)
    emailIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
    try {
        startActivity(Intent.createChooser(emailIntent, getString(R.string.contact_us_chooser_text)))
    } catch (ex: android.content.ActivityNotFoundException) {
        Toast.makeText(this, R.string.error_contact_us_no_email_client, Toast.LENGTH_SHORT).show()
    }
}

fun Context.updateWidgets() = sendBroadcast(Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE))

fun Context.openPlayStorePage() {
    try {
        startActivity(Intent(
                Intent.ACTION_VIEW,
                Uri.parse(getString(R.string.play_store_deep_link))
        ).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        })
    } catch (e: Exception) {
        Timber.e(e)

        // Open in browser
        startActivity(Intent(
                Intent.ACTION_VIEW,
                Uri.parse(getString(R.string.play_store_url))
        ).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        })
    }
}
