package com.kevalpatel2106.yip.repo.db

import android.app.Application
import com.kevalpatel2106.yip.entity.ProgressType
import com.kevalpatel2106.yip.repo.R
import com.kevalpatel2106.yip.repo.dto.ProgressDto
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

@RunWith(JUnit4::class)
class PrebuiltProcessBuilderTest {
    private val testTitleYear = "year"
    private val testTitleToday = "today"
    private val testTitleMonth = "month"
    private val testTitleQuarter = "quarter"
    private val testTitleWeek = "week"

    @Mock
    lateinit var application: Application

    private lateinit var prebuiltProgresses: List<ProgressDto>

    @Before
    fun before() {
        MockitoAnnotations.initMocks(this)
        Mockito.`when`(application.getString(R.string.this_year)).thenReturn(testTitleYear)
        Mockito.`when`(application.getString(R.string.today)).thenReturn(testTitleToday)
        Mockito.`when`(application.getString(R.string.this_month)).thenReturn(testTitleMonth)
        Mockito.`when`(application.getString(R.string.this_quarter)).thenReturn(testTitleQuarter)
        Mockito.`when`(application.getString(R.string.this_week)).thenReturn(testTitleWeek)

        prebuiltProgresses = PrebuiltProcessBuilder.getPrebuiltProgresses(application)
    }

    @Test
    fun checkProgressSize() {
        assertEquals(ProgressType.values().size - 1, prebuiltProgresses.size)
    }

    @Test
    fun checkExpectedProgressTypesPresent() {
        assertTrue(prebuiltProgresses.any { it.progressType == ProgressType.DAY_PROGRESS })
        assertTrue(prebuiltProgresses.any { it.progressType == ProgressType.YEAR_PROGRESS })
        assertTrue(prebuiltProgresses.any { it.progressType == ProgressType.WEEK_PROGRESS })
        assertTrue(prebuiltProgresses.any { it.progressType == ProgressType.MONTH_PROGRESS })
        assertTrue(prebuiltProgresses.any { it.progressType == ProgressType.QUARTER_PROGRESS })
    }

    @Test
    fun checkThereIsNoCustomProgress() {
        assertFalse(prebuiltProgresses.any { it.progressType == ProgressType.CUSTOM })
    }

    @Test
    fun checkTitles() {
        prebuiltProgresses.forEach {
            val expectedTitle = when (it.progressType) {
                ProgressType.DAY_PROGRESS -> testTitleToday
                ProgressType.YEAR_PROGRESS -> testTitleYear
                ProgressType.WEEK_PROGRESS -> testTitleWeek
                ProgressType.MONTH_PROGRESS -> testTitleMonth
                ProgressType.QUARTER_PROGRESS -> testTitleQuarter
                else -> throw IllegalArgumentException("Expected title not defined.")
            }

            assertEquals(expectedTitle, it.title)
        }
    }

    @Test
    fun checkColors() {
        prebuiltProgresses.forEach { assertEquals(it.progressType.color, it.color) }
    }

    @Test
    fun checkDiffBetweenStartDateAndEndDate() {
        prebuiltProgresses.forEach {
            assertTrue(it.end.time - it.start.time == PrebuiltProcessBuilder.DIFF_MILLS_START_AND_END_DATE)
        }
    }
}
