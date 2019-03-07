package com.kevalpatel2106.yip.detail

import android.content.Context
import android.content.Intent
import com.kevalpatel2106.yip.R
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class DetailUseCaseTest {
    private val testString = "test string"
    private val testTitle = "test title"

    @Mock
    internal lateinit var context: Context

    @Before
    fun before() {
        MockitoAnnotations.initMocks(this@DetailUseCaseTest)
        Mockito.`when`(context.getString(R.string.achivement_share_message, testTitle))
                .thenReturn(testString)
    }

    @Test
    fun checkShareAchievementIntentAction() {
        val shareTitle = DetailUseCase.prepareShareAchievementIntent(context, testTitle)
        Assert.assertEquals(Intent.ACTION_SEND, shareTitle.action)
    }

    @Test
    fun checkShareAchievementIntentType() {
        val shareTitle = DetailUseCase.prepareShareAchievementIntent(context, testTitle)
        Assert.assertEquals("text/plain", shareTitle.type)
    }

    @Test
    fun checkShareAchievementIntentText_nullTitle() {
        val shareTitle = DetailUseCase.prepareShareAchievementIntent(context, null)
        Assert.assertNull(shareTitle.getStringExtra(Intent.EXTRA_TEXT))
    }

    @Test
    fun checkShareAchievementIntentText_notNullTitle() {
        val shareTitle = DetailUseCase.prepareShareAchievementIntent(context, testTitle)
        Assert.assertEquals(testString, shareTitle.getStringExtra(Intent.EXTRA_TEXT))
    }
}