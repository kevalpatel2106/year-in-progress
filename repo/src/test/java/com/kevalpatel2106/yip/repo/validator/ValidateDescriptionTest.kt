package com.kevalpatel2106.yip.repo.validator

import android.app.Application
import android.content.res.Resources
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.MockitoAnnotations

@RunWith(Parameterized::class)
class ValidateDescriptionTest(
    private val description: String,
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
        whenever(application.resources).thenReturn(resources)
        whenever(resources.getInteger(ArgumentMatchers.anyInt())).thenReturn(10)

        validator = ValidatorImpl(application)
    }

    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun data(): ArrayList<Array<out Any?>> {
            return arrayListOf(
                arrayOf(null, true),
                arrayOf("", true),
                arrayOf("1", true),
                arrayOf("1234567", true),
                arrayOf("1234567       ", true),
                arrayOf("12345678901", false)
            )
        }
    }

    @Test
    fun validateTitle() {
        Assert.assertEquals(isValid, validator.isValidDescription(description))
    }
}
