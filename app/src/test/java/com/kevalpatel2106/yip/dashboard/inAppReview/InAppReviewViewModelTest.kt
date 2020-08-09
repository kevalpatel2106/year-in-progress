package com.kevalpatel2106.yip.dashboard.inAppReview

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import com.google.android.play.core.review.ReviewManager
import com.google.android.play.core.review.testing.FakeReviewManager
import com.kevalpatel2106.testutils.getKFixture
import com.kevalpatel2106.testutils.getOrAwaitValue
import com.kevalpatel2106.yip.dashboard.inAppReview.InAppReviewViewModel.Companion.RANDOM_NUMBER_FOR_RATING
import com.kevalpatel2106.yip.repo.sharedPrefs.SharedPrefsProvider
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.ArgumentMatchers.anyBoolean
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner

@RunWith(Enclosed::class)
class InAppReviewViewModelTest {

    @RunWith(JUnit4::class)
    class JUnitTest {

        @JvmField
        @Rule
        val rule = InstantTaskExecutorRule()

        @Mock
        lateinit var prefsProvider: SharedPrefsProvider

        @Mock
        lateinit var appManager: ReviewManager

        private lateinit var model: InAppReviewViewModel
        private val kFixture = getKFixture()

        @Before
        fun before() {
            MockitoAnnotations.initMocks(this)
            model = InAppReviewViewModel(appManager, prefsProvider)
        }

        @Test
        fun `given never ask rating true when review event triggered check review not asked`() {
            // given
            whenever(prefsProvider.getBoolFromPreference(anyString(), anyBoolean()))
                .thenReturn(true)

            // when
            model.onReviewEventTriggered(RANDOM_NUMBER_FOR_RATING)

            // check
            assertNull(model.inAppReviewState.value)
        }

        @Test
        fun `given never ask rating false but random number not for rating when review event triggered check review not asked`() {
            // given
            whenever(prefsProvider.getBoolFromPreference(anyString(), anyBoolean()))
                .thenReturn(true)

            // when
            model.onReviewEventTriggered(kFixture.range(RANDOM_NUMBER_FOR_RATING + 1..10))

            // check
            assertNull(model.inAppReviewState.value)
        }

        @Test
        fun `given never ask rating false and random number is for rating when review event triggered check confirmation dialog shown`() {
            // given
            whenever(prefsProvider.getBoolFromPreference(anyString(), anyBoolean()))
                .thenReturn(false)

            // when
            model.onReviewEventTriggered(RANDOM_NUMBER_FOR_RATING)

            // check
            assertEquals(
                InAppReviewViewState.ShowConfirmationDialog,
                model.inAppReviewState.getOrAwaitValue()
            )
        }

        @Test
        fun `when review later selected check should ask for rating set to false`() {
            // when
            model.onReviewLater()

            // check
            val boolCaptor = argumentCaptor<Boolean>()
            verify(prefsProvider).savePreferences(anyString(), boolCaptor.capture())
            assertFalse(boolCaptor.lastValue)
        }

        @Test
        fun `when review never selected check should ask for rating set to true`() {
            // when
            model.onReviewNever()

            // check
            val boolCaptor = argumentCaptor<Boolean>()
            verify(prefsProvider).savePreferences(anyString(), boolCaptor.capture())
            assertTrue(boolCaptor.lastValue)
        }

        @Test
        fun `when review process complete check should ask for rating set to true`() {
            // when
            model.onReviewProcessComplete()

            // check
            val boolCaptor = argumentCaptor<Boolean>()
            verify(prefsProvider).savePreferences(anyString(), boolCaptor.capture())
            assertTrue(boolCaptor.lastValue)
        }
    }

    @RunWith(RobolectricTestRunner::class)
    class RobolectricTest {
        private lateinit var fakeReviewManager: FakeReviewManager
        private lateinit var model: InAppReviewViewModel

        @Before
        fun before() {
            fakeReviewManager = FakeReviewManager(ApplicationProvider.getApplicationContext())
            model = InAppReviewViewModel(fakeReviewManager, mock())
        }

        @Test
        fun `when review now selected check should ask for rating set to true`() {
            // when
            model.onReviewNow()

            // check
            assertTrue(
                model.inAppReviewState.getOrAwaitValue() is InAppReviewViewState.LaunchReviewFlow
            )
        }
    }
}
