package com.kevalpatel2106.yip.repo.db

import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class DbTypeFloatingListConversationTest {
    @Test
    fun checkToFloatListFromString() {
        val floatList = arrayListOf(1F, 2F, 3F, 4F, 5F, 6F, 7F, 8F, 9F)
        val commaString = floatList.joinToString(",")

        DbTypeConverter.toFloatsList(commaString).forEachIndexed { index, value ->
            Assert.assertEquals(floatList[index], value)
        }
    }

    @Test
    fun checkToCommaSeparatedListFromFloatList() {
        val floatList = arrayListOf(1F, 2F, 3F, 4F, 5F, 6F, 7F, 8F, 9F)
        val commaString = floatList.joinToString(",")

        Assert.assertEquals(commaString, DbTypeConverter.toCommaSeparatedList(floatList))
    }

}
