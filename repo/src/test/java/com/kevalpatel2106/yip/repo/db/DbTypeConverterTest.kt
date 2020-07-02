package com.kevalpatel2106.yip.repo.db

import android.graphics.Color
import com.kevalpatel2106.yip.entity.DeadlineColor
import com.kevalpatel2106.yip.entity.DeadlineType
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
    class DeadlineTypeConversationTest {
        @Test
        fun checkToDeadlineTypeFromKey() {
            DeadlineType.values().forEach {
                assertEquals(it, DbTypeConverter.toType(it.key))
            }
        }

        @Test
        fun checkToDeadlineTypeFromUnknownKey() {
            assertEquals(DeadlineType.CUSTOM, DbTypeConverter.toType(12))
        }

        @Test
        fun checkToKeyFromDeadlineType() {
            DeadlineType.values().forEach {
                assertEquals(it.key, DbTypeConverter.fromType(it))
            }
        }
    }

    @RunWith(RobolectricTestRunner::class)
    class DeadlineColorConverterTest {

        @Test
        fun checkToDeadlineColorFromColorInt() {
            DeadlineColor.values().forEach {
                assertEquals(it, DbTypeConverter.toDeadlineColor(it.colorInt))
            }
        }

        @Test
        fun checkToDeadlineColorFromUnknownColorInt() {
            assertEquals(
                DeadlineColor.COLOR_GRAY,
                DbTypeConverter.toDeadlineColor(Color.TRANSPARENT)
            )
        }

        @Test
        fun checkToColorIntFromDeadlineColor() {
            DeadlineColor.values().forEach {
                assertEquals(it.colorInt, DbTypeConverter.fromColor(it))
            }
        }
    }
}
