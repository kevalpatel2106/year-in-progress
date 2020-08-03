package com.kevalpatel2106.yip.entity

import android.content.Intent
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.flextrade.kfixture.KFixture
import com.flextrade.kfixture.customisation.IgnoreDefaultArgsConstructorCustomisation
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import java.util.Date
import java.util.concurrent.TimeUnit

@RunWith(AndroidJUnit4::class)
class DeadlineParcelableTest {
    private val argParcelable = "parcel"
    private val kFixture = KFixture { add(IgnoreDefaultArgsConstructorCustomisation()) }
    private val deadline = Deadline(
        id = kFixture(),
        title = kFixture(),
        description = kFixture(),
        color = DeadlineColor.COLOR_YELLOW,
        end = Date(
            System.currentTimeMillis() - TimeUnit.MILLISECONDS.convert(
                1,
                TimeUnit.DAYS
            )
        ),
        start = Date(System.currentTimeMillis()),
        deadlineType = DeadlineType.YEAR_DEADLINE,
        notificationPercent = kFixture(),
        percent = kFixture()
    )

    @Test
    @Throws(Exception::class)
    fun test_parcelable() {
        val dateParcelIntent = Intent().apply { putExtra(argParcelable, deadline) }

        assertEquals(deadline, dateParcelIntent.getParcelableExtra<Deadline>(argParcelable))
    }
}
