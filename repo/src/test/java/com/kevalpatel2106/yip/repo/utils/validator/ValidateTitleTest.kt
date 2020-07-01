package com.kevalpatel2106.yip.repo.utils.validator

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
class ValidateTitleTest(
    private val title: String,
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

        validator =
            ValidatorImpl(application)
    }

    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun data(): ArrayList<Array<out Any?>> {
            return arrayListOf(
                arrayOf("", false),
                arrayOf("1", true),
                arrayOf("12345678901", true),
                arrayOf("123456789011234567890", false)
            )
        }
    }

    @Test
    fun validateTitle() {
        Assert.assertEquals(isValid, validator.isValidTitle(title))
    }
}
