package com.kevalpatel2106.yip.repo.nightModeRepo

import androidx.annotation.IntDef
import androidx.appcompat.app.AppCompatDelegate

@IntDef(
    AppCompatDelegate.MODE_NIGHT_YES,
    AppCompatDelegate.MODE_NIGHT_NO,
    AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
)
@Retention(AnnotationRetention.SOURCE)
annotation class NightModeValue
