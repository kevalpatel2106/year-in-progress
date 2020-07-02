package com.kevalpatel2106.yip.repo.db

import android.app.Application
import com.kevalpatel2106.yip.entity.DeadlineType
import com.kevalpatel2106.yip.repo.R
import com.kevalpatel2106.yip.repo.dto.DeadlineDto
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
class PrebuiltDeadlineBuilderTest {
    private val testTitleYear = "year"
    private val testTitleToday = "today"
    private val testTitleMonth = "month"
    private val testTitleQuarter = "quarter"
    private val testTitleWeek = "week"

    @Mock
    lateinit var application: Application

    private lateinit var prebuiltDeadlines: List<DeadlineDto>

    @Before
    fun before() {
        MockitoAnnotations.initMocks(this)
        Mockito.`when`(application.getString(R.string.this_year)).thenReturn(testTitleYear)
        Mockito.`when`(application.getString(R.string.today)).thenReturn(testTitleToday)
        Mockito.`when`(application.getString(R.string.this_month)).thenReturn(testTitleMonth)
        Mockito.`when`(application.getString(R.string.this_quarter)).thenReturn(testTitleQuarter)
        Mockito.`when`(application.getString(R.string.this_week)).thenReturn(testTitleWeek)

        prebuiltDeadlines = PrebuiltDeadlineBuilder.getPrebuiltDeadline(application)
    }

    @Test
    fun checkDeadlineSize() {
        assertEquals(DeadlineType.values().size - 1, prebuiltDeadlines.size)
    }

    @Test
    fun checkExpectedDeadlineTypesPresent() {
        assertTrue(prebuiltDeadlines.any { it.type == DeadlineType.DAY_DEADLINE })
        assertTrue(prebuiltDeadlines.any { it.type == DeadlineType.YEAR_DEADLINE })
        assertTrue(prebuiltDeadlines.any { it.type == DeadlineType.WEEK_DEADLINE })
        assertTrue(prebuiltDeadlines.any { it.type == DeadlineType.MONTH_DEADLINE })
        assertTrue(prebuiltDeadlines.any { it.type == DeadlineType.QUARTER_DEADLINE })
    }

    @Test
    fun checkThereIsNoCustomDeadline() {
        assertFalse(prebuiltDeadlines.any { it.type == DeadlineType.CUSTOM })
    }

    @Test
    fun checkTitles() {
        prebuiltDeadlines.forEach {
            val expectedTitle = when (it.type) {
                DeadlineType.DAY_DEADLINE -> testTitleToday
                DeadlineType.YEAR_DEADLINE -> testTitleYear
                DeadlineType.WEEK_DEADLINE -> testTitleWeek
                DeadlineType.MONTH_DEADLINE -> testTitleMonth
                DeadlineType.QUARTER_DEADLINE -> testTitleQuarter
                else -> throw IllegalArgumentException("Expected title not defined.")
            }

            assertEquals(expectedTitle, it.title)
        }
    }

    @Test
    fun checkColors() {
        prebuiltDeadlines.forEach { assertEquals(it.type.color, it.color) }
    }

    @Test
    fun checkDiffBetweenStartDateAndEndDate() {
        prebuiltDeadlines.forEach {
            assertTrue(it.end.time - it.start.time == PrebuiltDeadlineBuilder.DIFF_MILLS_START_AND_END_DATE)
        }
    }
}
