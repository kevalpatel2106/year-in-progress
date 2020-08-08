package com.kevalpatel2106.yip.dashboard.inAppUpdate

import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.install.model.UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS
import com.google.android.play.core.install.model.UpdateAvailability.UNKNOWN
import com.google.android.play.core.install.model.UpdateAvailability.UPDATE_AVAILABLE
import com.google.android.play.core.install.model.UpdateAvailability.UPDATE_NOT_AVAILABLE
import com.kevalpatel2106.yip.repo.sharedPrefs.SharedPrefsProvider
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.junit.runners.Parameterized
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyLong
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import java.util.concurrent.TimeUnit.HOURS

@RunWith(Enclosed::class)
class IsUpdateDownloadableTest {

    @RunWith(Parameterized::class)
    class ParameterizedTest(
        private val updateAvailability: Int,
        private val isUpdateTypeAllowed: Boolean,
        private val lastAskedHoursAgo: Long,
        private val output: Boolean
    ) {
        @Mock
        lateinit var prefsProvider: SharedPrefsProvider

        private lateinit var appUpdateHelper: InAppUpdateHelper

        @Before
        fun before() {
            MockitoAnnotations.initMocks(this)
            appUpdateHelper = InAppUpdateHelperImpl(prefsProvider)
        }

        @Test
        fun `given install status and last asked time when is update downloadable called check output`() {
            // given
            val updateInfo = mock<AppUpdateInfo>()
            whenever(updateInfo.updateAvailability()).thenReturn(updateAvailability)
            whenever(updateInfo.isUpdateTypeAllowed(anyInt())).thenReturn(isUpdateTypeAllowed)
            whenever(prefsProvider.getLongFromPreference(anyString(), anyLong()))
                .thenReturn(System.currentTimeMillis() + HOURS.toMillis(lastAskedHoursAgo))

            // when
            val downloadable = appUpdateHelper.isUpdateDownloadable(updateInfo)

            // check
            assertEquals(output, downloadable)
        }

        companion object {
            @JvmStatic
            @Parameterized.Parameters(name = "{index}: update availability = {0}, last asked hours ago = {2}, updated allowed = {1}, is downloadable = {3}")
            fun data(): ArrayList<Array<Any>> {
                return arrayListOf(
                    // the perfect condition
                    arrayOf(UPDATE_AVAILABLE, true, 25, true),

                    // Check possible last time asked values
                    arrayOf(UPDATE_AVAILABLE, true, -1, false),
                    arrayOf(UPDATE_AVAILABLE, true, 0, false),
                    arrayOf(UPDATE_AVAILABLE, true, 23, false),

                    // check possible update availability values
                    arrayOf(UPDATE_NOT_AVAILABLE, true, 25, false),
                    arrayOf(DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS, true, 25, false),
                    arrayOf(UNKNOWN, true, 25, false),

                    // check possible update allowed values
                    arrayOf(UPDATE_AVAILABLE, false, 25, false)
                )
            }
        }
    }

    @RunWith(JUnit4::class)
    class BlankPreferenceTest {
        @Mock
        lateinit var prefsProvider: SharedPrefsProvider

        @Mock
        lateinit var updateInfo: AppUpdateInfo

        private lateinit var appUpdateHelper: InAppUpdateHelper

        @Before
        fun before() {
            MockitoAnnotations.initMocks(this)
            appUpdateHelper = InAppUpdateHelperImpl(prefsProvider)
            whenever(updateInfo.updateAvailability()).thenReturn(UPDATE_AVAILABLE)
            whenever(updateInfo.isUpdateTypeAllowed(anyInt())).thenReturn(true)
            whenever(prefsProvider.getLongFromPreference(anyString(), anyLong()))
                .thenReturn(-1 /* Default preference value */)
        }

        @Test
        fun `given no last ask time in preference when is update downloadable called check downloadable`() {
            // when
            val downloadable = appUpdateHelper.isUpdateDownloadable(updateInfo)

            // check
            assertTrue(downloadable)
        }
    }
}
