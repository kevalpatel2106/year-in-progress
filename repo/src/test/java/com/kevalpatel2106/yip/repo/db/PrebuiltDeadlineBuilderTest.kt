package com.kevalpatel2106.yip.repo.db

import android.app.Application
import com.flextrade.kfixture.KFixture
import com.flextrade.kfixture.customisation.IgnoreDefaultArgsConstructorCustomisation
import com.kevalpatel2106.yip.entity.DeadlineType
import com.kevalpatel2106.yip.repo.R
import com.kevalpatel2106.yip.repo.dto.DeadlineDto
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

@RunWith(JUnit4::class)
class PrebuiltDeadlineBuilderTest {
    private val kFixture = KFixture { add(IgnoreDefaultArgsConstructorCustomisation()) }
    private val testTitleYear = kFixture<String>()
    private val testTitleToday = kFixture<String>()
    private val testTitleMonth = kFixture<String>()
    private val testTitleQuarter = kFixture<String>()
    private val testTitleWeek = kFixture<String>()

    @Mock
    lateinit var application: Application

    private lateinit var prebuiltDeadlines: List<DeadlineDto>

    @Before
    fun before() {
        MockitoAnnotations.initMocks(this)
        whenever(application.getString(R.string.this_year)).thenReturn(testTitleYear)
        whenever(application.getString(R.string.today)).thenReturn(testTitleToday)
        whenever(application.getString(R.string.this_month)).thenReturn(testTitleMonth)
        whenever(application.getString(R.string.this_quarter)).thenReturn(testTitleQuarter)
        whenever(application.getString(R.string.this_week)).thenReturn(testTitleWeek)

        prebuiltDeadlines = PrebuiltDeadlineBuilder.getPrebuiltDeadline(application)
    }

    @Test
    fun checkDeadlineSize() {
        assertEquals(DeadlineType.values().size - 1, prebuiltDeadlines.size)
    }

    @Test
    fun checkExpectedDeadlineTypesPresent() {
        DeadlineType.values()
            .filter { it != DeadlineType.CUSTOM }
            .forEach { type -> prebuiltDeadlines.any { it.type == type } }
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
    fun checkDescription() {
        prebuiltDeadlines.forEach { assertNull(it.description) }
    }

    @Test
    fun checkDiffBetweenStartDateAndEndDate() {
        prebuiltDeadlines.forEach {
            assertTrue(it.end.time - it.start.time == PrebuiltDeadlineBuilder.DIFF_MILLS_START_AND_END_DATE)
        }
    }
}
