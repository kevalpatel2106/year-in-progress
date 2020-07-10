package com.kevalpatel2106.yip.edit

import com.kevalpatel2106.testutils.getOrAwaitValue
import com.nhaarman.mockitokotlin2.verify
import junit.framework.Assert.assertFalse
import junit.framework.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
internal class EditViewDeadlineMonitorPremiumStatusTest : EditViewDeadlineModelTestSetUp() {

    @Test
    fun `when view model initialised check premium status monitoring starts`() {
        // check
        verify(billingRepo).observeIsPurchased()
    }

    @Test
    fun `given user is pro when monitoring premium status check lock not displayed on color list`() {
        // given
        userPremiumStatus.onNext(true)

        // check
        assertFalse(viewModel.viewState.getOrAwaitValue().showLockedColorPicker)
    }

    @Test
    fun `given user is not-pro when monitoring premium status check lock displayed on color list`() {
        // given
        userPremiumStatus.onNext(false)

        // check
        assertTrue(viewModel.viewState.getOrAwaitValue().showLockedColorPicker)
    }

    @Test
    fun `given user is pro when monitoring premium status check is premium flag set to true`() {
        // given
        userPremiumStatus.onNext(true)

        // check
        assertTrue(viewModel.isPremiumUser)
    }

    @Test
    fun `given user is not-pro when monitoring premium status check is premium flag set to false`() {
        // given
        userPremiumStatus.onNext(false)

        // check
        assertFalse(viewModel.isPremiumUser)
    }
}
