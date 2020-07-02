package com.kevalpatel2106.yip.edit

import com.kevalpatel2106.yip.core.emptyString
import com.kevalpatel2106.yip.core.setToDayMax
import com.kevalpatel2106.yip.core.setToDayMin
import com.kevalpatel2106.yip.entity.DeadlineColor
import com.kevalpatel2106.yip.entity.DeadlineType
import java.util.Calendar
import java.util.Date

internal data class EditViewState(
    val isLoading: Boolean,
    val isSomethingChanged: Boolean,

    val type: DeadlineType,

    val initialTitle: String,
    val currentTitle: String,
    val titleErrorMsg: String?,

    val allowEditDate: Boolean,
    val startTime: Date,
    val endTime: Date,

    val selectedColor: DeadlineColor,
    val lockColorPicker: Boolean,

    val lockNotification: Boolean,
    val notificationList: List<Float>
) {

    fun isTitleChanged(): Boolean = initialTitle.trim() != currentTitle.trim()

    companion object {

        internal fun initialState(): EditViewState {
            val tomorrow = Calendar.getInstance().apply { add(Calendar.DAY_OF_MONTH, 1) }

            return EditViewState(
                isLoading = false,
                isSomethingChanged = false,

                type = DeadlineType.CUSTOM,

                initialTitle = emptyString(),
                currentTitle = emptyString(),
                titleErrorMsg = null,

                allowEditDate = true,
                startTime = Date(System.currentTimeMillis()).apply { setToDayMin() },
                endTime = Date(tomorrow.timeInMillis).apply { setToDayMax() },

                selectedColor = DeadlineColor.COLOR_GRAY,
                lockColorPicker = true,

                lockNotification = true,
                notificationList = emptyList()
            )
        }
    }
}
