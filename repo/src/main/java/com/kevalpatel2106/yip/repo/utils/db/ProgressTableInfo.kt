package com.kevalpatel2106.yip.repo.utils.db

object ProgressTableInfo {
    const val TABLE_NAME = "progresses"
    const val ID = "id"
    const val TYPE = "type"
    const val TITLE = "title"
    const val START_TIME = "start_mills"
    const val END_TIME = "end_mills"
    const val COLOR = "color"

    @Deprecated("This field will be remove in next DB version upgrade.")
    const val ORDER = "order"

    @Deprecated("This field will be remove in next DB version upgrade.")
    const val IS_ENABLED = "is_enabled"
}