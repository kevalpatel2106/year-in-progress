package com.kevalpatel2106.yip.edit

import com.flextrade.kfixture.KFixture
import com.flextrade.kfixture.customisation.IgnoreDefaultArgsConstructorCustomisation
import com.kevalpatel2106.yip.core.emptyString
import com.kevalpatel2106.yip.entity.DeadlineColor
import com.kevalpatel2106.yip.entity.DeadlineType
import com.kevalpatel2106.yip.repo.dateFormatter.DateFormatter
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import java.util.Calendar

@RunWith(JUnit4::class)
class EditViewInitialStateTest {

    @Mock
    lateinit var dateFormatter: DateFormatter

    private val kFixture: KFixture = KFixture { add(IgnoreDefaultArgsConstructorCustomisation()) }
    private val formattedDateString = kFixture<String>()
    private lateinit var initialState: EditViewState

    @Before
    fun before() {
        MockitoAnnotations.initMocks(this)
        whenever(dateFormatter.formatDateOnly(any())).thenReturn(formattedDateString)

        initialState = EditViewState.initialState(dateFormatter)
    }

    @Test
    fun `given initial view state check is loading false`() {
        assertFalse(initialState.isLoading)
    }

    @Test
    fun `given initial view state check initial title ie empty`() {
        assertEquals(emptyString(), initialState.initialTitle)
    }

    @Test
    fun `given initial view state check current title is empty`() {
        assertEquals(emptyString(), initialState.currentTitle)
    }

    @Test
    fun `given initial view state check initial description is empty`() {
        assertEquals(emptyString(), initialState.initialDescription)
    }

    @Test
    fun `given initial view state check current description is empty`() {
        assertEquals(emptyString(), initialState.currentDescription)
    }

    @Test
    fun `given initial view state check title error is null`() {
        assertNull(initialState.titleErrorMsg)
    }

    @Test
    fun `given initial view state check deadline type is custom`() {
        assertEquals(DeadlineType.CUSTOM, initialState.type)
    }

    @Test
    fun `given initial view state check edit date not allowed`() {
        assertTrue(initialState.allowEditDate)
    }

    @Test
    fun `given initial view state check start date`() {
        val nowCal = Calendar.getInstance()

        val startCal = Calendar.getInstance().apply { time = initialState.startTime }
        assertEquals(nowCal.get(Calendar.DAY_OF_MONTH), startCal.get(Calendar.DAY_OF_MONTH))
        assertEquals(nowCal.get(Calendar.MONTH), startCal.get(Calendar.MONTH))
        assertEquals(nowCal.get(Calendar.YEAR), startCal.get(Calendar.YEAR))
        assertEquals(0, startCal.get(Calendar.HOUR_OF_DAY))
        assertEquals(0, startCal.get(Calendar.MINUTE))
        assertEquals(0, startCal.get(Calendar.SECOND))
        assertEquals(0, startCal.get(Calendar.MILLISECOND))
    }

    @Test
    fun `given initial view state check start date string`() {
        assertEquals(formattedDateString, initialState.startTimeString)
    }

    @Test
    fun `given initial view state check end date`() {
        val nextDayCal = Calendar.getInstance().apply { add(Calendar.DAY_OF_MONTH, 1) }

        val endCal = Calendar.getInstance().apply { time = initialState.endTime }
        assertEquals(nextDayCal.get(Calendar.DAY_OF_MONTH), endCal.get(Calendar.DAY_OF_MONTH))
        assertEquals(nextDayCal.get(Calendar.MONTH), endCal.get(Calendar.MONTH))
        assertEquals(nextDayCal.get(Calendar.YEAR), endCal.get(Calendar.YEAR))
        assertEquals(23, endCal.get(Calendar.HOUR_OF_DAY))
        assertEquals(59, endCal.get(Calendar.MINUTE))
        assertEquals(59, endCal.get(Calendar.SECOND))
        assertEquals(999, endCal.get(Calendar.MILLISECOND))
    }

    @Test
    fun `given initial view state check end date string`() {
        assertEquals(formattedDateString, initialState.endTimeString)
    }

    @Test
    fun `given initial view state check edit color not allowed`() {
        assertTrue(initialState.allowEditColor)
    }

    @Test
    fun `given initial view state check selected color is gray date`() {
        assertEquals(DeadlineColor.COLOR_GRAY, initialState.selectedColor)
    }

    @Test
    fun `given initial view state check locked color is showed`() {
        assertTrue(initialState.showLockedColorPicker)
    }

    @Test
    fun `given initial view state check notification list empty`() {
        assertTrue(initialState.notificationList.isEmpty())
    }
}
