package com.kevalpatel2106.yip.edit

import com.kevalpatel2106.yip.entity.ProgressColor
import com.kevalpatel2106.yip.entity.ProgressType

internal data class EditViewState(
        val progressType: ProgressType,

        val titleText: String,
        val titleErrorMsg: String?,

        val allowEditDate: Boolean,
        val progressStartTimeText: String,
        val progressEndTimeText: String,

        val progressColor: ProgressColor,
        val progressColors: List<Int>,
        val lockColorPicker: Boolean,
        val allowColorPickerClick: Boolean,

        val notificationList: List<Float>,

        val activityTitle: String
)