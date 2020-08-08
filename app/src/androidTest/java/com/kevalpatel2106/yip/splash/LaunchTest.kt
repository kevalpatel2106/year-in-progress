package com.kevalpatel2106.yip.splash

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import androidx.test.rule.ActivityTestRule
import com.kevalpatel2106.yip.R
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@MediumTest
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class LaunchTest {

    @Rule
    @JvmField
    var activityTestRule = ActivityTestRule(SplashActivity::class.java)

    @Rule
    @JvmField
    var hiltRule = HiltAndroidRule(this)

    @Test
    fun checkDashboardBottomNavBarAndFabVisible() {
        onView(withId(R.id.bottom_app_bar)).check(matches(isDisplayed()))
        onView(withId(R.id.add_deadline_fab)).check(matches(isDisplayed()))
    }
}
