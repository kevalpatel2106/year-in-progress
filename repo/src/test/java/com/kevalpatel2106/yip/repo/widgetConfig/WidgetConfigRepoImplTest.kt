package com.kevalpatel2106.yip.repo.widgetConfig

import com.kevalpatel2106.testutils.getKFixture
import com.kevalpatel2106.yip.entity.WidgetConfig
import com.kevalpatel2106.yip.entity.WidgetConfigContent
import com.kevalpatel2106.yip.entity.WidgetConfigTheme
import com.kevalpatel2106.yip.repo.sharedPrefs.SharedPrefsProvider
import com.kevalpatel2106.yip.repo.widgetConfig.WidgetConfigRepoImpl.Companion.WIDGET_IDS
import com.nhaarman.mockitokotlin2.anyOrNull
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.atLeast
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.MockitoAnnotations

@RunWith(JUnit4::class)
class WidgetConfigRepoImplTest {
    private val kFixture = getKFixture()

    @Mock
    lateinit var prefsProvider: SharedPrefsProvider

    private lateinit var widgetConfigRepo: WidgetConfigRepoImpl

    @Before
    fun before() {
        MockitoAnnotations.initMocks(this)
        widgetConfigRepo = WidgetConfigRepoImpl(prefsProvider)
    }

    @Test
    fun `given widget content when save config called check content stored to preference`() {
        // given
        val widgetId = kFixture<Int>()
        val content = WidgetConfigContent.PERCENT

        // when
        widgetConfigRepo.saveWidgetConfig(widgetId, content, WidgetConfigTheme.LIGHT)

        // check
        val prefKeyCaptor = argumentCaptor<String>()
        val prefValueCaptor = argumentCaptor<String>()
        verify(prefsProvider, atLeast(1))
            .savePreferences(prefKeyCaptor.capture(), prefValueCaptor.capture())
        assertTrue(prefKeyCaptor.allValues.contains(getPrefKeyContent(widgetId)))
        assertTrue(prefValueCaptor.allValues.contains(content.value))
    }

    @Test
    fun `given widget theme when save config called check theme stored to preference`() {
        // given
        val widgetId = kFixture<Int>()
        val theme = WidgetConfigTheme.LIGHT

        // when
        widgetConfigRepo.saveWidgetConfig(widgetId, WidgetConfigContent.PERCENT, theme)

        // check
        val prefKeyCaptor = argumentCaptor<String>()
        val prefValueCaptor = argumentCaptor<String>()
        verify(prefsProvider, atLeast(1))
            .savePreferences(prefKeyCaptor.capture(), prefValueCaptor.capture())
        assertTrue(prefKeyCaptor.allValues.contains(getPrefKeyTheme(widgetId)))
        assertTrue(prefValueCaptor.allValues.contains(theme.value))
    }

    @Test
    fun `given widget config stored when get widget config called check config values`() {
        // given
        val storedConfig = generateWidgetConfig()
        whenever(prefsProvider.getStringFromPreference(getPrefKeyContent(storedConfig.id)))
            .thenReturn(storedConfig.content.value)
        whenever(prefsProvider.getStringFromPreference(getPrefKeyTheme(storedConfig.id)))
            .thenReturn(storedConfig.theme.value)

        // when
        val config = widgetConfigRepo.getWidgetConfig(storedConfig.id)

        // check
        assertEquals(storedConfig, config)
    }

    @Test
    fun `given widget config not stored when get widget config called check default config returned`() {
        // given
        val widgetId = kFixture<Int>()
        whenever(prefsProvider.getStringFromPreference(anyString(), anyOrNull())).thenReturn(null)

        // when
        val config = widgetConfigRepo.getWidgetConfig(widgetId)

        // check
        assertEquals(widgetId, config.id)
        assertEquals(WidgetConfigTheme.LIGHT, config.theme)
        assertEquals(WidgetConfigContent.PERCENT, config.content)
    }

    @Test
    fun `given stored widget config values invalid when get widget config called check default config returned`() {
        // given
        val widgetId = kFixture<Int>()
        whenever(prefsProvider.getStringFromPreference(getPrefKeyContent(widgetId)))
            .thenReturn(kFixture())
        whenever(prefsProvider.getStringFromPreference(getPrefKeyTheme(widgetId)))
            .thenReturn(kFixture())

        // when
        val config = widgetConfigRepo.getWidgetConfig(widgetId)

        // check
        assertEquals(widgetId, config.id)
        assertEquals(WidgetConfigTheme.LIGHT, config.theme)
        assertEquals(WidgetConfigContent.PERCENT, config.content)
    }

    @Test
    fun `given widget id stored when delete widget id check widget id removed from preference`() {
        // given
        val widgetId = kFixture<Int>()
        val widgetIdToDelete = kFixture<Int>()
        whenever(prefsProvider.getStringFromPreference(WIDGET_IDS))
            .thenReturn("$widgetIdToDelete,$widgetId")

        // when
        widgetConfigRepo.deleteWidgetIds(intArrayOf(widgetId))

        // check
        val prefStringCaptor = argumentCaptor<String>()
        verify(prefsProvider).savePreferences(anyString(), prefStringCaptor.capture())
        assertEquals("$widgetIdToDelete", prefStringCaptor.lastValue)
    }

    @Test
    fun `given widget id not stored when delete widget id check widget id removed from preference`() {
        // given
        val widgetId = kFixture<Int>()
        whenever(prefsProvider.getStringFromPreference(WIDGET_IDS))
            .thenReturn("$widgetId")

        // when
        widgetConfigRepo.deleteWidgetIds(intArrayOf(kFixture()))

        // check
        val prefStringCaptor = argumentCaptor<String>()
        verify(prefsProvider).savePreferences(anyString(), prefStringCaptor.capture())
        assertEquals("$widgetId", prefStringCaptor.lastValue)
    }

    @Test
    fun `given no widget id stored when save new widget id check widget ids stored in preference`() {
        // given
        val widgetId = kFixture<Int>()
        val widgetIdToAdd = kFixture<Int>()
        whenever(prefsProvider.getStringFromPreference(WIDGET_IDS))
            .thenReturn("$widgetId")

        // when
        widgetConfigRepo.saveWidgetIds(widgetIdToAdd)

        // check
        val prefStringCaptor = argumentCaptor<String>()
        verify(prefsProvider).savePreferences(anyString(), prefStringCaptor.capture())
        assertEquals("$widgetId,$widgetIdToAdd", prefStringCaptor.lastValue)
    }

    @Test
    fun `given widget id stored when save widget id again check widget ids stored in preference`() {
        // given
        val widgetId = kFixture<Int>()
        whenever(prefsProvider.getStringFromPreference(WIDGET_IDS)).thenReturn("$widgetId")

        // when
        widgetConfigRepo.saveWidgetIds(widgetId)

        // check
        val prefStringCaptor = argumentCaptor<String>()
        verify(prefsProvider).savePreferences(anyString(), prefStringCaptor.capture())
        assertEquals("$widgetId", prefStringCaptor.lastValue)
    }

    @Test
    fun `given no widget id stored when get widget id called check no ids returned`() {
        // given
        whenever(prefsProvider.getStringFromPreference(WIDGET_IDS)).thenReturn("")

        // when
        val storedWidgetIds = widgetConfigRepo.getWidgetIds()

        // check
        assertTrue(storedWidgetIds.isEmpty())
    }

    @Test
    fun `given widget ids stored when get widget id called check returned ids`() {
        // given
        val widgetId1 = kFixture<Int>()
        val widgetId2 = kFixture<Int>()
        whenever(prefsProvider.getStringFromPreference(WIDGET_IDS))
            .thenReturn("$widgetId1,$widgetId2")

        // when
        val storedWidgetIds = widgetConfigRepo.getWidgetIds()

        // check
        assertEquals(widgetId1, storedWidgetIds[0])
        assertEquals(widgetId2, storedWidgetIds[1])
    }

    private fun generateWidgetConfig() =
        WidgetConfig(kFixture(), WidgetConfigTheme.DARK, WidgetConfigContent.TIME_LEFT)

    private fun getPrefKeyTheme(widgetId: Int): String =
        widgetId.toString() + WidgetConfigRepoImpl.PREF_WIDGET_ID_PREFIX + "theme"

    private fun getPrefKeyContent(widgetId: Int): String =
        widgetId.toString() + WidgetConfigRepoImpl.PREF_WIDGET_ID_PREFIX + "content"
}
