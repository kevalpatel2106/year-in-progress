package com.kevalpatel2106.yip.dashboard.inAppUpdate

import com.google.android.play.core.install.InstallState
import com.google.android.play.core.install.model.InstallStatus
import com.kevalpatel2106.testutils.getKFixture
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized


@RunWith(Parameterized::class)
class IsUpdateDownloadedTest(private val installStatus: Int, private val output: Boolean) {
    private lateinit var appUpdateHelper: InAppUpdateHelper

    @Before
    fun before() {
        appUpdateHelper = InAppUpdateHelperImpl(mock())
    }

    @Test
    fun `given install status when is updated downloaded called check output`() {
        // given
        val installState = mock<InstallState>()
        whenever(installState.installStatus()).thenReturn(installStatus)

        // when
        val isDownloaded = appUpdateHelper.isUpdateDownloaded(installState)

        // check
        Assert.assertEquals(output, isDownloaded)
    }

    companion object {
        private val kFixture = getKFixture()

        @JvmStatic
        @Parameterized.Parameters
        fun data(): ArrayList<Array<Any>> {
            return arrayListOf(
                arrayOf(InstallStatus.DOWNLOADED, true),
                arrayOf(InstallStatus.CANCELED, false),
                arrayOf(InstallStatus.DOWNLOADING, false),
                arrayOf(InstallStatus.FAILED, false),
                arrayOf(InstallStatus.INSTALLING, false),
                arrayOf(InstallStatus.PENDING, false),
                arrayOf(InstallStatus.UNKNOWN, false),
                arrayOf(kFixture.range(0 until InstallStatus.DOWNLOADED), false)
            )
        }
    }
}
