package com.kevalpatel2106.yip.dashboard.inAppUpdate

import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.install.model.UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS
import com.google.android.play.core.install.model.UpdateAvailability.UNKNOWN
import com.google.android.play.core.install.model.UpdateAvailability.UPDATE_AVAILABLE
import com.google.android.play.core.install.model.UpdateAvailability.UPDATE_NOT_AVAILABLE
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.mockito.MockitoAnnotations

@RunWith(Parameterized::class)
class IsUpdateDownloadableTest(
    private val updateAvailability: Int,
    private val stalenessDays: Int?,
    private val output: Boolean
) {
    private lateinit var appUpdateHelper: InAppUpdateHelper

    @Before
    fun before() {
        MockitoAnnotations.initMocks(this)
        appUpdateHelper = InAppUpdateHelperImpl()
    }

    @Test
    fun `given install status and last asked time when is update downloadable called check output`() {
        // given
        val updateInfo = mock<AppUpdateInfo>()
        whenever(updateInfo.updateAvailability()).thenReturn(updateAvailability)
        whenever(updateInfo.clientVersionStalenessDays()).thenReturn(stalenessDays)

        // when
        val downloadable = appUpdateHelper.isUpdateDownloadable(updateInfo)

        // check
        assertEquals(output, downloadable)
    }

    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "{index}: update availability = {0}, staleness days = {1}, is downloadable = {2}")
        fun data(): ArrayList<Array<out Any?>> {
            return arrayListOf(
                // the perfect condition
                arrayOf(UPDATE_AVAILABLE, 1, true),

                // staleness time conditions
                arrayOf(UPDATE_AVAILABLE, 3, true),
                arrayOf(UPDATE_AVAILABLE, 7, true),
                arrayOf(UPDATE_AVAILABLE, 8, false),
                arrayOf(UPDATE_AVAILABLE, null, true),

                // check possible update availability values
                arrayOf(UPDATE_NOT_AVAILABLE, 1, false),
                arrayOf(DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS, 1, false),
                arrayOf(UNKNOWN, 1, false)
            )
        }
    }
}
