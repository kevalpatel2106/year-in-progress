package com.kevalpatel2106.yip.detail

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.text.SpannableString
import com.kevalpatel2106.yip.core.emptyString
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class DetailViewStateTest {
    private val testTitle = "test title"
    private val testPercent = 92
    private val testPercentText = "$testPercent%"
    private val testStartTime = "test start time"
    private val testEndTime = "test end time"
    private val testTimeLeftText = SpannableString("test time left")
    private val testIsDeleting = true
    private val testCardBackGround = GradientDrawable()
    private val testDeadlineColor = Color.WHITE
    private val testDeadlineFlipperPos = DetailViewFlipper.POS_SHARE

    private lateinit var state: DetailViewState

    @Before
    fun before() {
        state = DetailViewState(
            titleText = testTitle,
            percent = testPercent,
            deadlinePercentText = testPercentText,
            startTimeText = testStartTime,
            endTimeText = testEndTime,
            timeLeftText = testTimeLeftText,
            isDeleting = testIsDeleting,
            cardBackground = testCardBackGround,
            deadlineColor = testDeadlineColor,
            detailFlipperPosition = testDeadlineFlipperPos,
            showRepeatable = false
        )
    }

    @Test
    fun checkConstructor() {
        assertEquals(testTitle, state.titleText)
        assertEquals(testPercent, state.percent)
        assertEquals(testPercentText, state.deadlinePercentText)
        assertEquals(testStartTime, state.startTimeText)
        assertEquals(testEndTime, state.endTimeText)
        assertEquals(testTimeLeftText, state.timeLeftText)
        assertEquals(testIsDeleting, state.isDeleting)
        assertEquals(testCardBackGround, state.cardBackground)
        assertEquals(testDeadlineColor, state.deadlineColor)
        assertEquals(testDeadlineFlipperPos, state.detailFlipperPosition)
        assertFalse(state.showRepeatable)
    }

    @Test
    fun testEquals() {
        val sameObject = state.copy()
        val noCardColor = state.copy(cardBackground = null)
        val differentPercent = state.copy(percent = 23)

        assertEquals(sameObject, state)
        assertNotEquals(noCardColor, state)
        assertNotEquals(differentPercent, state)
        assertNotEquals(emptyString(), state)
        assertNotEquals(null, state)
    }

    @Test
    fun testHashcode() {
        val sameObject = state.copy()
        val noCardColor = state.copy(cardBackground = null)
        val differentPercent = state.copy(percent = 23)

        assertEquals(sameObject.hashCode(), state.hashCode())
        assertNotEquals(noCardColor.hashCode(), state.hashCode())
        assertNotEquals(differentPercent.hashCode(), state.hashCode())
        assertNotEquals(emptyString().hashCode(), state.hashCode())
    }
}
