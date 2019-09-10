package com.kevalpatel2106.yip.detail

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.text.SpannableString
import com.kevalpatel2106.yip.core.emptyString
import org.junit.Assert.assertEquals
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
    private val testIsDeletingProgress = true
    private val testCardBackGround = GradientDrawable()
    private val testProgressColor = Color.WHITE
    private val testProgressFlipperPos = ProgressFlipper.POS_SHARE_PROGRESS

    private lateinit var state: DetailViewState

    @Before
    fun before() {
        state = DetailViewState(
            progressTitleText = testTitle,
            progressPercent = testPercent,
            progressPercentText = testPercentText,
            progressStartTimeText = testStartTime,
            progressEndTimeText = testEndTime,
            progressTimeLeftText = testTimeLeftText,
            isDeletingProgress = testIsDeletingProgress,
            cardBackground = testCardBackGround,
            progressColor = testProgressColor,
            detailFlipperPosition = testProgressFlipperPos
        )
    }

    @Test
    fun checkConstructor() {
        assertEquals(testTitle, state.progressTitleText)
        assertEquals(testPercent, state.progressPercent)
        assertEquals(testPercentText, state.progressPercentText)
        assertEquals(testStartTime, state.progressStartTimeText)
        assertEquals(testEndTime, state.progressEndTimeText)
        assertEquals(testTimeLeftText, state.progressTimeLeftText)
        assertEquals(testIsDeletingProgress, state.isDeletingProgress)
        assertEquals(testCardBackGround, state.cardBackground)
        assertEquals(testProgressColor, state.progressColor)
        assertEquals(testProgressFlipperPos, state.detailFlipperPosition)
    }

    @Test
    fun testEquals() {
        val sameObject = state.copy()
        val noCardColor = state.copy(cardBackground = null)
        val differentPercent = state.copy(progressPercent = 23)

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
        val differentPercent = state.copy(progressPercent = 23)

        assertEquals(sameObject.hashCode(), state.hashCode())
        assertNotEquals(noCardColor.hashCode(), state.hashCode())
        assertNotEquals(differentPercent.hashCode(), state.hashCode())
        assertNotEquals(emptyString().hashCode(), state.hashCode())
    }
}
