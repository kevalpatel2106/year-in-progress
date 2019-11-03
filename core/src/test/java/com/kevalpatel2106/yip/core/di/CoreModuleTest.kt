package com.kevalpatel2106.yip.core.di

import android.app.AlarmManager
import android.app.Application
import android.appwidget.AppWidgetManager
import android.content.Context
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.times
import org.mockito.MockitoAnnotations

@RunWith(JUnit4::class)
class CoreModuleTest {

    @Mock
    lateinit var application: Application

    @Mock
    lateinit var alarmManager: AlarmManager

    @Mock
    lateinit var appWidgetManager: AppWidgetManager

    @Captor
    lateinit var stringArgumentCaptor: ArgumentCaptor<String>

    private lateinit var coreModule: CoreModule

    @Before
    fun before() {
        MockitoAnnotations.initMocks(this@CoreModuleTest)
        Mockito.`when`(application.getSystemService(Context.ALARM_SERVICE)).thenReturn(alarmManager)
        Mockito.`when`(application.getSystemService(Context.APPWIDGET_SERVICE))
            .thenReturn(appWidgetManager)
        coreModule = CoreModule(application)
    }

    @Test
    fun checkProvideAlarmManager() {
        val am = coreModule.provideAlarmManager()
        Mockito.verify(application, times(1))
            .getSystemService(stringArgumentCaptor.capture())
        assertEquals(Context.ALARM_SERVICE, stringArgumentCaptor.value)
        assertEquals(alarmManager, am)
    }

    @Test
    fun checkProvideAppWidgetService() {
        val am = coreModule.provideAppWidgetService()
        Mockito.verify(application, times(1))
            .getSystemService(stringArgumentCaptor.capture())
        assertEquals(Context.APPWIDGET_SERVICE, stringArgumentCaptor.value)
        assertEquals(appWidgetManager, am)
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
