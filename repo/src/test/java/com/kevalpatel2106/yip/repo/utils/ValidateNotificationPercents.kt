package com.kevalpatel2106.yip.repo.utils

import android.app.Application
import android.content.res.Resources
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations


@RunWith(Parameterized::class)
class ValidateNotificationPercents(
    private val notification: List<Float>,
    private val isValid: Boolean
) {
    private lateinit var validator: Validator

    @Mock
    lateinit var application: Application

    @Mock
    lateinit var resources: Resources

    @Before
    fun before() {
        MockitoAnnotations.initMocks(this)
        Mockito.`when`(application.resources).thenReturn(resources)
        Mockito.`when`(resources.getInteger(ArgumentMatchers.anyInt())).thenReturn(20)

        validator = Validator(application)
    }

    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun data(): ArrayList<Array<out Any?>> {
            return arrayListOf(
                arrayOf(arrayListOf(0F, 2F), true),
                arrayOf(arrayListOf(100F, 1F), true),
                arrayOf(arrayListOf(1F, 100F), true),
                arrayOf(arrayListOf(-1F, 4F), false),
                arrayOf(arrayListOf(1F, 101F), false)
            )
        }
    }

    @Test
    fun validateTitle() {
        Assert.assertEquals(isValid, validator.isValidNotification(notification))
    }
}
