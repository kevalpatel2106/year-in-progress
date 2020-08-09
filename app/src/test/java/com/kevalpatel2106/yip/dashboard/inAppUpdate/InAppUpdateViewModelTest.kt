package com.kevalpatel2106.yip.dashboard.inAppUpdate

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.tasks.Task
import com.kevalpatel2106.testutils.getOrAwaitValue
import com.kevalpatel2106.yip.dashboard.inAppUpdate.InAppUpdateViewState.NotifyUpdateAvailable
import com.kevalpatel2106.yip.dashboard.inAppUpdate.InAppUpdateViewState.NotifyUpdateReadyToInstall
import com.kevalpatel2106.yip.dashboard.inAppUpdate.InAppUpdateViewState.StartUpdateDownload
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.never
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.MockitoAnnotations

@RunWith(JUnit4::class)
class InAppUpdateViewModelTest {

    @JvmField
    @Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    lateinit var mockUpdateManager: AppUpdateManager

    @Mock
    lateinit var mockUpdateHelper: InAppUpdateHelper

    @Mock
    lateinit var mockUpdateInfoTask: Task<AppUpdateInfo>

    @Before
    fun before() {
        MockitoAnnotations.initMocks(this)
        whenever(mockUpdateManager.appUpdateInfo).thenReturn(mockUpdateInfoTask)
    }

    @Test
    fun `when view model initialise check update info listener registers`() {
        // when
        InAppUpdateViewModel(mockUpdateManager, mockUpdateHelper)

        // check
        verify(mockUpdateInfoTask).addOnSuccessListener(any())
    }

    @Test
    fun `when view model initialise check update install listener registers`() {
        // when
        InAppUpdateViewModel(mockUpdateManager, mockUpdateHelper)

        // check
        verify(mockUpdateManager).registerListener(any())
    }

    @Test
    fun `given view model initialise when download approved check view state changes to start download`() {
        // given
        val model = InAppUpdateViewModel(mockUpdateManager, mockUpdateHelper)

        // when
        model.onNewUpdateDownloadApproved(mock())

        // check
        assertTrue(model.inAppUpdateState.getOrAwaitValue() is StartUpdateDownload)
    }

    @Test
    fun `given view model initialise when install approved check complete update called`() {
        // given
        val model = InAppUpdateViewModel(mockUpdateManager, mockUpdateHelper)

        // when
        model.onNewUpdateInstallApproved()

        // check
        verify(mockUpdateManager).completeUpdate()
    }

    @Test
    fun `given update available and is downloadable when update info received check update available message displayed`() {
        // given
        val model = InAppUpdateViewModel(mockUpdateManager, mockUpdateHelper)
        whenever(mockUpdateHelper.isUpdateDownloadable(any())).thenReturn(true)

        // when
        model.onUpdateInfoReceived(mock())

        // check
        assertTrue(model.inAppUpdateState.getOrAwaitValue() is NotifyUpdateAvailable)
    }

    @Test
    fun `given update available and is downloadable when update info received check last check time resets`() {
        // given
        whenever(mockUpdateHelper.isUpdateDownloadable(any())).thenReturn(true)
        val model = InAppUpdateViewModel(mockUpdateManager, mockUpdateHelper)

        // when
        model.onUpdateInfoReceived(mock())

        // check
        verify(mockUpdateHelper).resetUpdateInfoAskedTime()
    }

    @Test
    fun `given update available and is not downloadable when update info received check no message displayed`() {
        // given
        val model = InAppUpdateViewModel(mockUpdateManager, mockUpdateHelper)
        whenever(mockUpdateHelper.isUpdateDownloadable(any())).thenReturn(false)

        // when
        model.onUpdateInfoReceived(mock())

        // check
        assertNull(model.inAppUpdateState.value)
    }

    @Test
    fun `given update available and is not downloadable when update info received check last check time don't reset`() {
        // given
        val model = InAppUpdateViewModel(mockUpdateManager, mockUpdateHelper)
        whenever(mockUpdateHelper.isUpdateDownloadable(any())).thenReturn(false)

        // when
        model.onUpdateInfoReceived(mock())

        // check
        verify(mockUpdateHelper, never()).resetUpdateInfoAskedTime()
    }

    @Test
    fun `given update not available when view model initialise check ready to install message displayed`() {
        // given
        val model = InAppUpdateViewModel(mockUpdateManager, mockUpdateHelper)
        whenever(mockUpdateHelper.isUpdateDownloaded(any())).thenReturn(true)

        // when
        model.onUpdateDownloadStateChanged(mock())

        // check
        assertTrue(model.inAppUpdateState.getOrAwaitValue() is NotifyUpdateReadyToInstall)
    }

    @Test
    fun `given update not available when view model initialise check no message displayed`() {
        // given
        val model = InAppUpdateViewModel(mockUpdateManager, mockUpdateHelper)
        whenever(mockUpdateHelper.isUpdateDownloadable(any())).thenReturn(false)

        // when
        model.onUpdateDownloadStateChanged(mock())

        // check
        assertNull(model.inAppUpdateState.value)
    }
}

