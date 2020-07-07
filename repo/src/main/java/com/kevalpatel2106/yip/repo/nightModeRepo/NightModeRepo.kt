package com.kevalpatel2106.yip.repo.nightModeRepo

import io.reactivex.Observable

interface NightModeRepo {
    fun observeNightModeChanges(): Observable<Int>

    @NightModeValue
    fun getNightModeSetting(): Int
}
