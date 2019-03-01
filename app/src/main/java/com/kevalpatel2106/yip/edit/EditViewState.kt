package com.kevalpatel2106.yip.edit

internal data class EditViewState(
        var enableColorEdit: Boolean,

        var allowEditDate: Boolean,

        var titleText: String,
        var titleErrorMsg: String,

        var progressStartTimeText: String,
        var progressEndTimeText: String
)