package com.kevalpatel2106.yip.entity.ext

import com.kevalpatel2106.yip.entity.DeadlineType

fun DeadlineType.isPreBuild() = this != DeadlineType.CUSTOM
fun DeadlineType.isRepeatable() = this != DeadlineType.CUSTOM
