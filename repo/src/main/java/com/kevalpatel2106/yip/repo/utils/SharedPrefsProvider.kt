/*
 * Copyright (c) 2018. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.kevalpatel2106.yip.repo.utils

import android.content.SharedPreferences

/**
 * Created by Keval on 01-Jun-18.
 * Class contains all the helper functions to deal with shared prefs. You need to call [SharedPrefsProvider.init]
 * to initialize the shared preferences in your application class.
 *
 * @author <a href="https://github.com/kevalpatel2106">kevalpatel2106</a>
 */
class SharedPrefsProvider(private val sharedPreference: SharedPreferences) {

    /**
     * Remove and clear data from preferences for given field
     *
     * @param key key of preference field to remove
     */
    fun removePreferences(key: String) {
        //Delete SharedPref
        val prefsEditor = sharedPreference.edit()
        prefsEditor.remove(key)
        prefsEditor.apply()
    }

    /**
     * Save value to shared preference
     *
     * @param key   key of preference field
     *
     * @param value value to store
     */
    fun savePreferences(key: String, value: String?) {
        //Save to share prefs
        val prefsEditor = sharedPreference.edit()
        prefsEditor.putString(key, value)
        prefsEditor.apply()
    }

    /**
     * Save value to shared preference
     *
     * @param key   key of preference field
     *
     * @param value value to store
     */
    fun savePreferences(key: String, value: Boolean) {
        //Save to share prefs
        val prefsEditor = sharedPreference.edit()
        prefsEditor.putBoolean(key, value)
        prefsEditor.apply()
    }

    /**
     * Save value to shared preference
     *
     * @param key   key of preference field
     *
     * @param value value to store in int
     */
    fun savePreferences(key: String, value: Int) {
        //Save to share prefs
        val prefsEditor = sharedPreference.edit()
        prefsEditor.putInt(key, value)
        prefsEditor.apply()
    }


    /**
     * Save value to shared preference
     *
     * @param key   key of preference field
     *
     * @param value value to store in long
     */
    fun savePreferences(key: String, value: Long) {
        //Save to share prefs
        val prefsEditor = sharedPreference.edit()
        prefsEditor.putLong(key, value)
        prefsEditor.apply()
    }

    /**
     * Read string from shared preference file

     * @param key : key of value field to read
     * *
     * @return string value for given key else null if key not found.
     */
    @JvmOverloads
    fun getStringFromPreferences(key: String, defVal: String? = null): String? = sharedPreference.getString(key, defVal)

    /**
     * Read string from shared preference file
     *
     * @param key : key of value field to read
     *
     * @return boolean value for given key else flase if key not found.
     */
    @JvmOverloads
    fun getBoolFromPreferences(key: String, defVal: Boolean = false): Boolean = sharedPreference.getBoolean(key, defVal)

    /**
     * Read string from shared preference file
     *
     * @param key : key of value field to read
     *
     * @return long value for given key else -1 if key not found.
     */
    @JvmOverloads
    fun getLongFromPreference(key: String, defVal: Long = -1): Long = sharedPreference.getLong(key, defVal)

    /**
     * Read string from shared preference file
     *
     * @param key : key of value field to read
     *
     * @return int value for given key else -1 if key not found.
     */
    @JvmOverloads
    fun getIntFromPreference(key: String, defVal: Int = -1): Int = sharedPreference.getInt(key, defVal)

    fun nukePrefrance() {
        sharedPreference.edit().clear().apply()
    }
}
