package com.kevalpatel2106.yip.core.ext

import android.app.Activity
import android.appwidget.AppWidgetManager
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.kevalpatel2106.feature.core.BuildConfig
import com.kevalpatel2106.feature.core.R

const val SNACK_BAR_DURATION = 2000L
private const val NEW_TASK_INTENT_FLAG =
    Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
private const val HIV_TO_COLOR_DIMENSION = 3
private const val GRADIENT_70 = 0.7F
private const val GRADIENT_80 = 0.8F
private const val GRADIENT_85 = 0.85F
private const val GRADIENT_90 = 0.9F

/**
 * Get the color from color res.
 */
fun Context.getColorCompat(@ColorRes color: Int) = ContextCompat.getColor(this, color)

fun <T : AppCompatActivity> Context.getLaunchIntent(
    aClass: Class<T>,
    isNewTask: Boolean = false,
    extras: (Intent.() -> Unit)? = null
): Intent {
    return Intent(this, aClass).apply {
        if (isNewTask) addFlags(NEW_TASK_INTENT_FLAG)
        extras?.invoke(this)
    }
}

/**
 * Display the snack bar.
 */
fun Activity.showSnack(
    message: String,
    duration: Int = SNACK_BAR_DURATION.toInt(),
    actonTitle: Int = -1,
    actionListener: ((View) -> Unit)? = null,
    dismissListener: (() -> Unit)? = null
): Snackbar {
    val snackbar = Snackbar.make(
        findViewById<ViewGroup>(android.R.id.content).getChildAt(0) as ViewGroup,
        message,
        duration
    ).apply {
        actonTitle.takeIf { it > 0 }?.let { title -> setAction(title, actionListener) }
        addCallback(object : BaseTransientBottomBar.BaseCallback<Snackbar>() {
            override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                super.onDismissed(transientBottomBar, event)
                dismissListener?.invoke()
            }
        })
    }
    snackbar.show()
    return snackbar
}

/**
 * Send an email to the development team.
 */
fun Context.sendMailToDev() {
    val emailTitle = "Query regarding ${BuildConfig.APPLICATION_NAME}"
    val emailText = """"
        <ENTER YOUR MESSAGE HERE>




        ------------------------------
        PLEASE DON'T REMOVE/EDIT BELOW INFO:
        DEVICE NAME : ${Build.MODEL} (${Build.DEVICE})
        MANUFACTURER : ${Build.MANUFACTURER}
        ANDROID VERSION : ${Build.VERSION.SDK_INT}
        APPLICATION VERSION : ${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})
        ------------------------------
        """.trimIndent()

    val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
        data = Uri.parse(getString(R.string.email_scheme))
        putExtra(Intent.EXTRA_EMAIL, resources.getStringArray(R.array.support_email))
        putExtra(Intent.EXTRA_SUBJECT, emailTitle)
        putExtra(Intent.EXTRA_TEXT, emailText)
        addFlags(NEW_TASK_INTENT_FLAG)
    }

    try {
        startActivity(
            Intent.createChooser(
                emailIntent,
                getString(R.string.contact_us_chooser_text)
            )
        )
    } catch (ex: ActivityNotFoundException) {
        Toast.makeText(this, R.string.error_contact_us_no_email_client, Toast.LENGTH_SHORT).show()
    }
}

fun Context.updateWidgets() = sendBroadcast(Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE))

@ColorInt
fun darkenColor(@ColorInt color: Int, factor: Float = 0.5f): Int {
    return Color.HSVToColor(FloatArray(HIV_TO_COLOR_DIMENSION).apply {
        Color.colorToHSV(color, this)
        this[2] *= factor
    })
}

fun Context.getBackgroundGradient(@ColorInt color: Int): GradientDrawable {
    val dark70 = darkenColor(color, GRADIENT_70)
    val dark80 = darkenColor(color, GRADIENT_80)
    val dark85 = darkenColor(color, GRADIENT_85)
    val dark90 = darkenColor(color, GRADIENT_90)

    val colors = intArrayOf(dark70, dark80, dark85, dark90, dark90, color)
    return GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, colors)
        .apply { cornerRadius = resources.getDimension(R.dimen.card_radius) }
}
