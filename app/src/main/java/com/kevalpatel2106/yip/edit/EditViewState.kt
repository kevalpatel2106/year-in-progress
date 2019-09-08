package com.kevalpatel2106.yip.edit

import com.kevalpatel2106.yip.core.emptyString
import com.kevalpatel2106.yip.core.setToDayMax
import com.kevalpatel2106.yip.core.setToDayMin
import com.kevalpatel2106.yip.entity.ProgressColor
import com.kevalpatel2106.yip.entity.ProgressType
import java.util.Calendar
import java.util.Date

internal data class EditViewState(
    val isLoadingProgress: Boolean,
    val isSomethingChanged: Boolean,

    val progressType: ProgressType,

    val initialTitle: String,
    val currentTitle: String,
    val titleErrorMsg: String?,
    val isTitleChanged: Boolean,

    val allowEditDate: Boolean,
    val progressStartTime: Date,
    val progressEndTime: Date,

    val progressColor: ProgressColor,
    val lockColorPicker: Boolean,

    val lockNotification: Boolean,
    val notificationList: List<Float>
) {

    companion object {

        internal fun initialState(): EditViewState {
            val tomorrow = Calendar.getInstance().apply { add(Calendar.DAY_OF_MONTH, 1) }

            return EditViewState(
                isLoadingProgress = false,
                isSomethingChanged = false,

                progressType = ProgressType.CUSTOM,

                initialTitle = emptyString(),
                currentTitle = emptyString(),
                titleErrorMsg = null,
                isTitleChanged = false,

                allowEditDate = true,
                progressStartTime = Date(System.currentTimeMillis()).apply { setToDayMin() },
                progressEndTime = Date(tomorrow.timeInMillis).apply { setToDayMax() },

                progressColor = ProgressColor.COLOR_GRAY,
                lockColorPicker = true,

                lockNotification = true,
                notificationList = emptyList()
            )
        }
    }
}
