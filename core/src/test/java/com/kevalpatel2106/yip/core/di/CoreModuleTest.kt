package com.kevalpatel2106.yip.core.di

import android.app.Application
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.MockitoAnnotations

@RunWith(JUnit4::class)
class CoreModuleTest {

    @Mock
    lateinit var application: Application

    private lateinit var coreModule: CoreModule

    @Before
    fun before() {
        MockitoAnnotations.initMocks(this@CoreModuleTest)
        coreModule = CoreModule(application)
    }

    @Test
    fun checkProvideApplicationInstance() {
        assertNotNull(coreModule.provideApplication())
        assertEquals(application, coreModule.provideApplication())
    }

    @Test
    fun checkProvideContextIsApplication() {
        assertNotNull(coreModule.provideContext())
        assertTrue(coreModule.provideContext() is Application)
    }
}