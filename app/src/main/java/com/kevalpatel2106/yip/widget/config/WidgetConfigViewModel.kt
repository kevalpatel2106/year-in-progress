package com.kevalpatel2106.yip.widget.config

import android.app.Activity
import android.appwidget.AppWidgetManager
import androidx.annotation.IdRes
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kevalpatel2106.yip.R
import com.kevalpatel2106.yip.core.BaseViewModel
import com.kevalpatel2106.yip.core.ext.getFirstKey
import com.kevalpatel2106.yip.core.livedata.SingleLiveEvent
import com.kevalpatel2106.yip.core.livedata.modify
import com.kevalpatel2106.yip.core.livedata.nullSafeValue
import com.kevalpatel2106.yip.entity.WidgetConfigContent
import com.kevalpatel2106.yip.entity.WidgetConfigTheme
import com.kevalpatel2106.yip.repo.widgetConfig.WidgetConfigRepo
import com.kevalpatel2106.yip.widget.config.WidgetConfigSingleEvent.CloseScreen
import com.kevalpatel2106.yip.widget.config.WidgetConfigUseCase.getPreviewImage

class WidgetConfigViewModel @ViewModelInject constructor(
    private val widgetConfigRepo: WidgetConfigRepo
) : BaseViewModel() {
    private val contentMap = hashMapOf(
        WidgetConfigContent.PERCENT to R.id.widget_config_percent_content_radio,
        WidgetConfigContent.TIME_LEFT to R.id.widget_config_time_left_content_radio
    )
    private val themeMap = hashMapOf(
        WidgetConfigTheme.DARK to R.id.widget_config_dark_theme_radio,
        WidgetConfigTheme.LIGHT to R.id.widget_config_light_theme_radio
    )

    private val _viewState = MutableLiveData<WidgetConfigViewState>(
        WidgetConfigViewState.initialState()
    )
    val viewState: LiveData<WidgetConfigViewState> = _viewState

    private val _singleEvent = SingleLiveEvent<WidgetConfigSingleEvent>()
    val singleEvent: LiveData<WidgetConfigSingleEvent> = _singleEvent

    fun setWidgetId(widgetId: Int) {
        if (widgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            _singleEvent.value = CloseScreen(Activity.RESULT_CANCELED, widgetId)
        } else {
            val widgetConfig = widgetConfigRepo.getWidgetConfig(widgetId)
            _viewState.modify {
                copy(
                    widgetId = widgetId,
                    selectedContentId = contentMap.getValue(widgetConfig.content),
                    previewImageRes = getPreviewImage(widgetConfig.content, widgetConfig.theme),
                    selectedThemeId = themeMap.getValue(widgetConfig.theme),
                    applyButtonEnabled = true
                )
            }
        }
    }

    fun onApplyWidgetConfig() {
        _viewState.modify { copy(applyButtonEnabled = false) }
        with(viewState.nullSafeValue()) {
            widgetConfigRepo.saveWidgetConfig(
                widgetId = widgetId,
                content = contentMap.getFirstKey(selectedContentId),
                theme = themeMap.getFirstKey(selectedThemeId)
            )
            _singleEvent.value = CloseScreen(Activity.RESULT_OK, widgetId)
        }
    }

    fun onWidgetConfigChanged(
        @IdRes selectedThemeRadioId: Int,
        @IdRes selectedContentRadioId: Int
    ) {
        if ((contentMap.containsValue(selectedContentRadioId)
                    && themeMap.containsValue(selectedThemeRadioId))
            && (viewState.nullSafeValue().selectedContentId != selectedContentRadioId
                    || viewState.nullSafeValue().selectedThemeId != selectedThemeRadioId)
        ) {
            _viewState.modify {
                copy(
                    selectedContentId = selectedContentRadioId,
                    selectedThemeId = selectedThemeRadioId,
                    previewImageRes = getPreviewImage(
                        content = contentMap.getFirstKey(selectedContentRadioId),
                        theme = themeMap.getFirstKey(selectedThemeRadioId)
                    )
                )
            }
        }
    }
}
