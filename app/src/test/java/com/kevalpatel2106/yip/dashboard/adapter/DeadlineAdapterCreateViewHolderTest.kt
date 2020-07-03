package com.kevalpatel2106.yip.dashboard.adapter

import android.widget.LinearLayout
import com.kevalpatel2106.yip.entity.Deadline
import org.junit.Assert.assertNotNull
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment

@RunWith(RobolectricTestRunner::class)
class DeadlineAdapterCreateViewHolderTest {
    private lateinit var adapter: DeadlineAdapter

    @Before
    fun setUp() {
        adapter = DeadlineAdapter(object : DeadlineAdapterEventListener {
            override fun onDeadlineClicked(deadline: Deadline) = Unit
        })
    }

    @Test
    fun `given invalid view type when creating view holder check it fails`() {
        try {
            adapter.createViewHolder(LinearLayout(RuntimeEnvironment.application), 3490)
            fail()
        } catch (e: IllegalStateException) {
            assertNotNull(e.message)
        }
    }
}
