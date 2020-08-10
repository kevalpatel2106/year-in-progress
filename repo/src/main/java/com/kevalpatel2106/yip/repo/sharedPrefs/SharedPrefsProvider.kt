package com.kevalpatel2106.yip.repo.sharedPrefs

import io.reactivex.Observable

interface SharedPrefsProvider {

    fun removePreferences(key: String)

    fun savePreferences(key: String, value: String?)

    fun savePreferences(key: String, value: Boolean)

    fun savePreferences(key: String, value: Int)

    fun savePreferences(key: String, value: Long)

    fun observeStringFromPreference(key: String, defVal: String? = null): Observable<String>

    fun observeBoolFromPreference(key: String, defVal: Boolean = false): Observable<Boolean>

    fun getStringFromPreference(key: String, defVal: String? = null): String?

    fun getBoolFromPreference(key: String, defVal: Boolean = false): Boolean

    fun getLongFromPreference(key: String, defVal: Long = -1): Long

    fun getIntFromPreference(key: String, defVal: Int = -1): Int
}
