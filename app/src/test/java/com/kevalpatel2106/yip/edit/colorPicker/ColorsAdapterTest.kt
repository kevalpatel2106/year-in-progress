package com.kevalpatel2106.yip.edit.colorPicker

import androidx.recyclerview.widget.RecyclerView
import com.kevalpatel2106.yip.entity.ProgressColor
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment


@RunWith(RobolectricTestRunner::class)
class ColorsAdapterTest {
    @Mock
    internal lateinit var colorSelectedListener: ColorPickerListener

    @Mock
    internal lateinit var dataObserver: RecyclerView.AdapterDataObserver

    private lateinit var adapter: ColorsAdapter

    @Before
    fun before() {
        MockitoAnnotations.initMocks(this)
        adapter = ColorsAdapter(RuntimeEnvironment.application, colorSelectedListener)
        adapter.registerAdapterDataObserver(dataObserver)
    }

    @After
    fun tearDown() {
        adapter.unregisterAdapterDataObserver(dataObserver)
    }

    @Test
    fun checkListOfColors() {
        Assert.assertTrue(adapter.colors.isNotEmpty())

        val supportedColor = ProgressColor.values()
        adapter.colors.forEachIndexed { index, colorStates ->
            Assert.assertEquals(colorStates, ColorStates(supportedColor[index].colorInt, false))
        }
    }

    @Test
    fun checkSettingLockRefreshesList() {
        adapter.isLocked = true
        Mockito.verify(dataObserver, Mockito.times(1)).onChanged()
    }

    @Test
    fun checkSetSelectedColor() {
        val selectedProgressColor = ProgressColor.COLOR_GRAY.colorInt
        adapter.setSelectedColor(selectedProgressColor)

        adapter.colors.forEach {
            Assert.assertEquals(it.color == selectedProgressColor, it.isSelected)
        }
        Mockito.verify(dataObserver, Mockito.times(1)).onChanged()
    }
}
