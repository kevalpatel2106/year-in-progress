package com.kevalpatel2106.yip.repo.db

import android.graphics.Color
import com.kevalpatel2106.yip.entity.ProgressColor
import com.kevalpatel2106.yip.entity.ProgressType
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.robolectric.RobolectricTestRunner
import java.util.Date

@RunWith(Enclosed::class)
class DbTypeConverterTest {

    @RunWith(JUnit4::class)
    class DateConversationTest {

        @Test
        fun checkToDate() {
            val currentMills = System.currentTimeMillis()
            assertEquals(Date(currentMills), DbTypeConverter.toDate(currentMills))
        }

        @Test
        fun checkToLongFromDate() {
            val currentMills = System.currentTimeMillis()
            assertEquals(currentMills, DbTypeConverter.toLong(Date(currentMills)))
        }

    }

    @RunWith(JUnit4::class)
    class FloatingListConversationTest {
        @Test
        fun checkToFloatListFromString() {
            val floatList = arrayListOf(1F, 2F, 3F, 4F, 5F, 6F, 7F, 8F, 9F)
            val commaString = floatList.joinToString(",")

            DbTypeConverter.toFloatsList(commaString).forEachIndexed { index, value ->
                assertEquals(floatList[index], value)
            }
        }

        @Test
        fun checkToCommaSeparatedListFromFloatList() {
            val floatList = arrayListOf(1F, 2F, 3F, 4F, 5F, 6F, 7F, 8F, 9F)
            val commaString = floatList.joinToString(",")

            assertEquals(commaString, DbTypeConverter.toCommaSeparatedList(floatList))
        }

    }

    @RunWith(JUnit4::class)
    class ProgressTypeConversationTest {
        @Test
        fun checkToProgressTypeFromKey() {
            ProgressType.values().forEach {
                assertEquals(it, DbTypeConverter.toType(it.key))
            }
        }

        @Test
        fun checkToProgressTypeFromUnknownKey() {
            assertEquals(ProgressType.CUSTOM, DbTypeConverter.toType(12))
        }

        @Test
        fun checkToKeyFromProgressType() {
            ProgressType.values().forEach {
                assertEquals(it.key, DbTypeConverter.fromType(it))
            }
        }
    }

    @RunWith(RobolectricTestRunner::class)
    class ProgressColorConverterTest {

        @Test
        fun checkToProgressColorFromColorInt() {
            ProgressColor.values().forEach {
                assertEquals(it, DbTypeConverter.toProgressColor(it.colorInt))
            }
        }

        @Test
        fun checkToProgressColorFromUnknownColorInt() {
            assertEquals(
                ProgressColor.COLOR_GRAY,
                DbTypeConverter.toProgressColor(Color.TRANSPARENT)
            )
        }

        @Test
        fun checkToColorIntFromProgressColor() {
            ProgressColor.values().forEach {
                assertEquals(it.colorInt, DbTypeConverter.fromColor(it))
            }
        }
    }
}
