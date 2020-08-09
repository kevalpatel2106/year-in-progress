package com.kevalpatel2106.yip.dashboard.inAppUpdate

import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.install.model.UpdateAvailability
import com.kevalpatel2106.testutils.getKFixture
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class IsUpdateAlreadyDownloadedTest(
    private val updateAvailability: Int,
    private val output: Boolean
) {
    private lateinit var appUpdateHelper: InAppUpdateHelper

    @Before
    fun before() {
        appUpdateHelper = InAppUpdateHelperImpl()
    }

    @Test
    fun `given update availability when is updated already downloaded called check output`() {
        // given
        val appUpdateInfo = mock<AppUpdateInfo>()
        whenever(appUpdateInfo.updateAvailability()).thenReturn(updateAvailability)

        // when
        val isDownloaded = appUpdateHelper.isUpdateAlreadyDownloaded(appUpdateInfo)

        // check
        Assert.assertEquals(output, isDownloaded)
    }

    companion object {
        private val kFixture = getKFixture()

        @JvmStatic
        @Parameterized.Parameters
        fun data(): ArrayList<Array<Any>> {
            return arrayListOf(
                arrayOf(UpdateAvailability.UPDATE_AVAILABLE, false),
                arrayOf(UpdateAvailability.UPDATE_NOT_AVAILABLE, false),
                arrayOf(UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS, true),
                arrayOf(UpdateAvailability.UNKNOWN, false)
            )
        }
    }
}
