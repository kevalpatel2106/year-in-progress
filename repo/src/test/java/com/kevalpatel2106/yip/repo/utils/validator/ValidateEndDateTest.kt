package com.kevalpatel2106.yip.repo.utils.validator

import android.app.Application
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import java.util.Date


@RunWith(Parameterized::class)
class ValidateEndDateTest(
    private val startDate: Date?,
    private val endDate: Date?,
    private val isValid: Boolean
) {
    private lateinit var validator: Validator

    @Mock
    lateinit var application: Application

    @Before
    fun before() {
        MockitoAnnotations.initMocks(this)
        validator =
            ValidatorImpl(application)
    }

    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun data(): ArrayList<Array<out Any?>> {
            return arrayListOf(
                arrayOf(
                    Date(System.currentTimeMillis()),
                    Date(System.currentTimeMillis() - 1000),
                    false
                ),
                arrayOf(
                    Date(System.currentTimeMillis()),
                    Date(System.currentTimeMillis() + 1000),
                    true
                ),
                arrayOf(Date(System.currentTimeMillis()), null, false),
                arrayOf(null, Date(System.currentTimeMillis() - 1000), false)
            )
        }
    }

    @Test
    fun validateStartDate() {
        Assert.assertEquals(isValid, validator.isValidEndDate(startDate, endDate))
    }
}
