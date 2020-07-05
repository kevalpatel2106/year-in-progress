package com.kevalpatel2106.yip.repo.sharedPrefs

import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.kevalpatel2106.testutils.RxSchedulersOverrideRule
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment

/**
 * Created by Keval on 19-Jul-17.
 * Test class for [SharedPrefsProvider].
 */
@RunWith(RobolectricTestRunner::class)
class SharedPrefsProviderImplTest {

    @JvmField
    @Rule
    val rule = RxSchedulersOverrideRule()

    private val testKey = "test_key"
    private lateinit var mockSharedPreference: SharedPreferences
    private lateinit var sharedPrefsProvider: SharedPrefsProvider

    @Before
    @Throws(Exception::class)
    fun setUp() {
        mockSharedPreference = PreferenceManager.getDefaultSharedPreferences(
            RuntimeEnvironment.application
        )
        sharedPrefsProvider = SharedPrefsProviderImpl(mockSharedPreference)
    }

    @Test
    @Throws(Exception::class)
    fun removePreferences() {
        mockSharedPreference.edit().apply {
            putString(testKey, "String")
            apply()
        }

        sharedPrefsProvider.removePreferences(testKey)
        assertNull(mockSharedPreference.getString(testKey, null))
    }

    @Test
    @Throws(Exception::class)
    fun savePreferences() {
        assertEquals(-1, mockSharedPreference.getInt(testKey, -1))
        sharedPrefsProvider.savePreferences(testKey, "String")
        assertTrue(mockSharedPreference.getString(testKey, null) != null)
    }

    @Test
    @Throws(Exception::class)
    fun savePreferences1() {
        assertEquals(-1, mockSharedPreference.getInt(testKey, -1))
        sharedPrefsProvider.savePreferences(testKey, 1)
        assertTrue(mockSharedPreference.getInt(testKey, -1) != -1)
    }

    @Test
    @Throws(Exception::class)
    fun savePreferences2() {
        assertEquals(-1, mockSharedPreference.getInt(testKey, -1))
        sharedPrefsProvider.savePreferences(testKey, 100000L)
        assertTrue(mockSharedPreference.getLong(testKey, -1) != -1L)
    }

    @Test
    @Throws(Exception::class)
    fun savePreferences3() {
        assertFalse(mockSharedPreference.getBoolean(testKey, false))
        sharedPrefsProvider.savePreferences(testKey, true)
        assertTrue(mockSharedPreference.getBoolean(testKey, false))
    }

    @Test
    @Throws(Exception::class)
    fun getStringFromPreferences() {
        val testVal = "String"
        mockSharedPreference.edit().apply {
            putString(testKey, testVal)
            apply()
        }

        assertTrue(sharedPrefsProvider.getStringFromPreference(testKey) == testVal)
    }

    @Test
    @Throws(Exception::class)
    fun getBoolFromPreferences() {
        val testVal = true
        mockSharedPreference.edit().apply {
            putBoolean(testKey, true)
            apply()
        }

        assertTrue(sharedPrefsProvider.getBoolFromPreference(testKey) == testVal)
    }

    @Test
    @Throws(Exception::class)
    fun getLongFromPreference() {
        val testVal = 100000L
        mockSharedPreference.edit().apply {
            putLong(testKey, testVal)
            apply()
        }

        assertTrue(sharedPrefsProvider.getLongFromPreference(testKey) == testVal)
    }

    @Test
    @Throws(Exception::class)
    fun getIntFromPreference() {
        val testVal = 100
        mockSharedPreference.edit().apply {
            putInt(testKey, testVal)
            apply()
        }

        assertTrue(sharedPrefsProvider.getIntFromPreference(testKey) == testVal)
    }

    @Test
    @Throws(Exception::class)
    fun observeStringFromPreference_whenDefaultValueNull() {
        // Given
        val testVal = "test"
        val testObserver = sharedPrefsProvider
            .observeStringFromPreference(testKey, null)
            .test()

        // When
        mockSharedPreference.edit().apply {
            putString(testKey, testVal)
            apply()
        }

        // Check
        testObserver.assertNotTerminated()
            .assertValueAt(0) { it.isEmpty() }
            .assertValueAt(1) { it == testVal }
    }

    @Test
    @Throws(Exception::class)
    fun observeStringFromPreference_whenDefaultValueNotNull() {
        // Given
        val testVal = "test"
        val defValue = "defValue"
        val testObserver = sharedPrefsProvider
            .observeStringFromPreference(testKey, defValue)
            .test()

        // When
        mockSharedPreference.edit().apply {
            putString(testKey, testVal)
            apply()
        }

        // Check
        testObserver.assertNotTerminated()
            .assertValueAt(0) { it == defValue }
            .assertValueAt(1) { it == testVal }
    }
}
