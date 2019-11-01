package com.kevalpatel2106.yip.dashboard

import android.content.Context
import com.kevalpatel2106.yip.R
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

@RunWith(JUnit4::class)
class DashboardUsecaseKtTest {
    private val testAdUnitId = "test ad id"
    @Mock
    lateinit var context: Context

    @Before
    fun before() {
        MockitoAnnotations.initMocks(this)
        Mockito.`when`(context.getString(R.string.detail_interstitial_ad_id))
            .thenReturn(testAdUnitId)
    }

    @Test
    fun checkInterstitialAdUnitId() {
        val interstitialAd = context.prepareInterstitialAd()
        assertEquals(testAdUnitId, interstitialAd.adUnitId)
    }
}
