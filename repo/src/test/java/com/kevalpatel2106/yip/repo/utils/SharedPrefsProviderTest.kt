/*
 * Copyright (c) 2018. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.kevalpatel2106.yip.repo.utils

import com.kevalpatel2106.testutils.MockSharedPreference
import com.kevalpatel2106.yip.repo.providers.SharedPrefsProvider
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

/**
 * Created by Keval on 19-Jul-17.
 * Test class for [SharedPrefsProvider].
 */
@RunWith(JUnit4::class)
class SharedPrefsProviderTest {

    private val TEST_KEY = "test_key"

    private lateinit var mockSharedPreference: MockSharedPreference

    private lateinit var sharedPrefsProvider: SharedPrefsProvider

    @Before
    @Throws(Exception::class)
    fun setUp() {
        mockSharedPreference = MockSharedPreference()
        sharedPrefsProvider = SharedPrefsProvider(mockSharedPreference)
    }

    @Test
    @Throws(Exception::class)
    fun removePreferences() {
        val editor = mockSharedPreference.edit()
        editor.putString(TEST_KEY, "String")
        editor.apply()

        sharedPrefsProvider.removePreferences(TEST_KEY)
        assertTrue(mockSharedPreference.getString(TEST_KEY, null) == null)
    }

    @Test
    @Throws(Exception::class)
    fun savePreferences() {
        assertFalse(mockSharedPreference.getInt(TEST_KEY, -1) != -1)
        sharedPrefsProvider.savePreferences(TEST_KEY, "String")
        assertTrue(mockSharedPreference.getString(TEST_KEY, null) != null)
    }

    @Test
    @Throws(Exception::class)
    fun savePreferences1() {
        assertFalse(mockSharedPreference.getInt(TEST_KEY, -1) != -1)
        sharedPrefsProvider.savePreferences(TEST_KEY, 1)
        assertTrue(mockSharedPreference.getInt(TEST_KEY, -1) != -1)
    }

    @Test
    @Throws(Exception::class)
    fun savePreferences2() {
        assertFalse(mockSharedPreference.getLong(TEST_KEY, -1) != -1L)
        sharedPrefsProvider.savePreferences(TEST_KEY, 100000L)
        assertTrue(mockSharedPreference.getLong(TEST_KEY, -1) != -1L)
    }

    @Test
    @Throws(Exception::class)
    fun savePreferences3() {
        assertFalse(mockSharedPreference.getBoolean(TEST_KEY, false))
        sharedPrefsProvider.savePreferences(TEST_KEY, true)
        assertTrue(mockSharedPreference.getBoolean(TEST_KEY, false))
    }

    @Test
    @Throws(Exception::class)
    fun getStringFromPreferences() {
        val testVal = "String"

        val editor = mockSharedPreference.edit()
        editor.putString(TEST_KEY, testVal)
        editor.apply()

        assertTrue(sharedPrefsProvider.getStringFromPreference(TEST_KEY) == testVal)
    }

    @Test
    @Throws(Exception::class)
    fun getBoolFromPreferences() {
        val testVal = true

        val editor = mockSharedPreference.edit()
        editor.putBoolean(TEST_KEY, true)
        editor.apply()


        assertTrue(sharedPrefsProvider.getBoolFromPreference(TEST_KEY) == testVal)
    }

    @Test
    @Throws(Exception::class)
    fun getLongFromPreference() {
        val testVal = 100000L

        val editor = mockSharedPreference.edit()
        editor.putLong(TEST_KEY, testVal)
        editor.apply()

        assertTrue(sharedPrefsProvider.getLongFromPreference(TEST_KEY) == testVal)
    }

    @Test
    @Throws(Exception::class)
    fun getIntFromPreference() {
        val testVal = 100

        val editor = mockSharedPreference.edit()
        editor.putInt(TEST_KEY, testVal)
        editor.apply()

        assertTrue(sharedPrefsProvider.getIntFromPreference(TEST_KEY) == testVal)
    }
}
