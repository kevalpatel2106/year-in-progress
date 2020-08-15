package com.kevalpatel2106.yip.widget.config

import android.app.Activity
import android.appwidget.AppWidgetManager
import android.content.Intent
import android.os.Bundle
import android.widget.RadioGroup
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.kevalpatel2106.yip.R
import com.kevalpatel2106.yip.core.ext.updateWidgets
import com.kevalpatel2106.yip.core.livedata.nullSafeObserve
import com.kevalpatel2106.yip.databinding.ActivityWidgetConfigBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_widget_config.widget_config_content_radio_group
import kotlinx.android.synthetic.main.activity_widget_config.widget_config_theme_radio_group


@AndroidEntryPoint
class WidgetConfigActivity : AppCompatActivity(), RadioGroup.OnCheckedChangeListener {
    private val model by viewModels<WidgetConfigViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView<ActivityWidgetConfigBinding>(
            this,
            R.layout.activity_widget_config
        ).apply {
            lifecycleOwner = this@WidgetConfigActivity
            viewModel = model
        }

        model.setWidgetId(
            intent?.extras?.getInt(
                AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID
            ) ?: AppWidgetManager.INVALID_APPWIDGET_ID
        )

        widget_config_theme_radio_group.setOnCheckedChangeListener(this)
        widget_config_content_radio_group.setOnCheckedChangeListener(this)
        monitorSingleEvent()
    }

    private fun monitorSingleEvent() {
        model.singleEvent.nullSafeObserve(this) { event ->
            when (event) {
                is WidgetConfigSingleEvent.CloseScreen -> {
                    if (event.resultCode == Activity.RESULT_OK) updateWidgets()
                    val resultValue = Intent()
                        .apply { putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, event.appWidgetId) }
                    setResult(event.resultCode, resultValue)
                    finish()
                }
            }
        }
    }

    override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
        model.onWidgetConfigChanged(
            widget_config_theme_radio_group.checkedRadioButtonId,
            widget_config_content_radio_group.checkedRadioButtonId
        )
    }
}
