package com.kevalpatel2106.testutils

import com.flextrade.kfixture.KFixture
import com.flextrade.kfixture.customisation.IgnoreDefaultArgsConstructorCustomisation

fun getKFixture() = KFixture { add(IgnoreDefaultArgsConstructorCustomisation()) }
