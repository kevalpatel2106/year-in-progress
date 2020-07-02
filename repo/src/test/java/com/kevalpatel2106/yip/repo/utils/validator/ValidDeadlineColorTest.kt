package com.kevalpatel2106.yip.repo.utils.validator

import android.app.Application
import androidx.annotation.ColorInt
import com.kevalpatel2106.yip.entity.DeadlineColor
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.mockito.Mock
import org.mockito.MockitoAnnotations

@RunWith(Parameterized::class)
class ValidDeadlineColorTest(
    @ColorInt private val value: Int?,
    private val isValidDeadline: Boolean
) {

    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun data(): ArrayList<Array<out Any?>> {
            return arrayListOf(
                arrayOf(null, false),
                arrayOf(1, false),
                arrayOf(-1, false),
                arrayOf(0, true),
                arrayOf(DeadlineColor.COLOR_BLUE.colorInt, true),
                arrayOf(DeadlineColor.COLOR_GREEN.colorInt, true),
                arrayOf(DeadlineColor.COLOR_TILL.colorInt, true),
                arrayOf(DeadlineColor.COLOR_ORANGE.colorInt, true),
                arrayOf(DeadlineColor.COLOR_YELLOW.colorInt, true),
                arrayOf(DeadlineColor.COLOR_PINK.colorInt, true),
                arrayOf(DeadlineColor.COLOR_GRAY.colorInt, true)
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
    fun checkIsValidDeadlineColor() {
        Assert.assertEquals(isValidDeadline, validator.isValidDeadlineColor(value))
    }
}
