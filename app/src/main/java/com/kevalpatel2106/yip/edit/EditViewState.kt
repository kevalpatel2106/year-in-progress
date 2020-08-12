package com.kevalpatel2106.yip.edit

import com.kevalpatel2106.yip.core.ext.emptyString
import com.kevalpatel2106.yip.core.ext.setToDayMax
import com.kevalpatel2106.yip.core.ext.setToDayMin
import com.kevalpatel2106.yip.entity.DeadlineColor
import com.kevalpatel2106.yip.entity.DeadlineType
import com.kevalpatel2106.yip.repo.dateFormatter.DateFormatter
import java.util.Calendar
import java.util.Date

internal data class EditViewState(
    val isLoading: Boolean,

    val type: DeadlineType,

    val initialTitle: String,
    val currentTitle: String,
    val titleErrorMsg: String?,

    val initialDescription: String,
    val currentDescription: String,

    val allowEditDate: Boolean,
    val startTime: Date,
    val startTimeString: String,
    val endTime: Date,
    val endTimeString: String,

    val allowEditColor: Boolean,
    val selectedColor: DeadlineColor,
    val showLockedColorPicker: Boolean,

    val allowEditNotifications: Boolean,
    val notificationList: List<Float>
) {

    fun isTitleChanged(): Boolean = initialTitle.trim() != currentTitle.trim()

    fun isDescriptionChanged(): Boolean = initialDescription.trim() != currentDescription.trim()

    companion object {

        internal fun initialState(df: DateFormatter): EditViewState {
            val tomorrow = Calendar.getInstance().apply { add(Calendar.DAY_OF_MONTH, 1) }
            val startTime = Date(System.currentTimeMillis()).apply { setToDayMin() }
            val endTime = Date(tomorrow.timeInMillis).apply { setToDayMax() }

            return EditViewState(
                isLoading = false,

                type = DeadlineType.CUSTOM,

                initialTitle = emptyString(),
                currentTitle = emptyString(),
                titleErrorMsg = null,

                initialDescription = emptyString(),
                currentDescription = emptyString(),

                allowEditDate = true,
                startTime = startTime,
                startTimeString = df.formatDateOnly(startTime),
                endTime = endTime,
                endTimeString = df.formatDateOnly(endTime),

                allowEditColor = true,
                selectedColor = DeadlineColor.COLOR_GRAY,
                showLockedColorPicker = true,

                allowEditNotifications = true,
                notificationList = emptyList()
            )
        }
    }
}
