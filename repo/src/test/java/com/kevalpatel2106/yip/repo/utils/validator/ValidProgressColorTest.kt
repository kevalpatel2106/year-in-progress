package com.kevalpatel2106.yip.repo.utils.validator

import android.app.Application
import androidx.annotation.ColorInt
import com.kevalpatel2106.yip.entity.ProgressColor
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.mockito.Mock
import org.mockito.MockitoAnnotations

@RunWith(Parameterized::class)
class ValidProgressColorTest(@ColorInt private val value: Int?, private val isValidProgress: Boolean) {

    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun data(): ArrayList<Array<out Any?>> {
            return arrayListOf(
                arrayOf(null, false),
                arrayOf(1, false),
                arrayOf(-1, false),
                arrayOf(0, true),
                arrayOf(ProgressColor.COLOR_BLUE.colorInt, true),
                arrayOf(ProgressColor.COLOR_GREEN.colorInt, true),
                arrayOf(ProgressColor.COLOR_TILL.colorInt, true),
                arrayOf(ProgressColor.COLOR_ORANGE.colorInt, true),
                arrayOf(ProgressColor.COLOR_YELLOW.colorInt, true),
                arrayOf(ProgressColor.COLOR_PINK.colorInt, true),
                arrayOf(ProgressColor.COLOR_GRAY.colorInt, true)
            )
        }
    }

    @Mock
    internal lateinit var application: Application

    private lateinit var validator: Validator

    @Before
    fun before() {
        MockitoAnnotations.initMocks(this)
        validator =
            ValidatorImpl(application)
    }

    @Test
    fun checkGetProgressColor() {
        Assert.assertEquals(isValidProgress, validator.isValidProgressColor(value))
    }
}
