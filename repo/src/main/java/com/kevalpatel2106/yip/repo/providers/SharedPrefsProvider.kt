/*
 * Copyright (c) 2018. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.kevalpatel2106.yip.repo.providers

import android.content.SharedPreferences
import androidx.core.content.edit
import com.f2prateek.rx.preferences2.RxSharedPreferences
import com.kevalpatel2106.yip.repo.utils.RxSchedulers
import io.reactivex.Observable

/**
 * Created by Keval on 20-Aug-16.
 * Class contains all the helper functions to deal with shared prefs.
 *
 * @author <a href="https://github.com/kevalpatel2106">kevalpatel2106</a>
 */
class SharedPrefsProvider internal constructor(private val sharedPreference: SharedPreferences) {
    private val rxPreferences = RxSharedPreferences.create(sharedPreference)

    /**
     * Remove and clear data from preferences for given field
     *
     * @param key key of preference field to remove
     */
    fun removePreferences(key: String) {
        sharedPreference.edit { remove(key) }
    }

    /**
     * Save value to shared preference
     *
     * @param key key of preference field
     *
     * @param value value to store
     */
    fun savePreferences(key: String, value: String?) {
        sharedPreference.edit { putString(key, value) }
    }

    /**
     * Save value to shared preference
     *
     * @param key key of preference field
     *
     * @param value value to store
     */
    fun savePreferences(key: String, value: Boolean) {
        sharedPreference.edit { putBoolean(key, value) }
    }

    /**
     * Save value to shared preference
     *
     * @param key key of preference field
     *
     * @param value value to store in int
     */
    fun savePreferences(key: String, value: Int) {
        sharedPreference.edit { putInt(key, value) }
    }

    /**
     * Save value to shared preference
     *
     * @param key key of preference field
     *
     * @param value value to store in long
     */
    fun savePreferences(key: String, value: Long) {
        sharedPreference.edit { putLong(key, value) }
    }

    @JvmOverloads
    fun observeStringFromPreference(key: String, defVal: String? = null): Observable<String> {
        return run {
            if (defVal != null) {
                rxPreferences.getString(key, defVal)
            } else {
                rxPreferences.getString(key)
            }
        }.asObservable()
                .subscribeOn(RxSchedulers.disk)
                .observeOn(RxSchedulers.main)
    }

    @JvmOverloads
    fun getStringFromPreference(key: String, defVal: String? = null): String? {
        return sharedPreference.getString(key, defVal)
    }

    @JvmOverloads
    fun getBoolFromPreference(key: String, defVal: Boolean = false): Boolean {
        return sharedPreference.getBoolean(key, defVal)
    }

    @JvmOverloads
    fun getLongFromPreference(key: String, defVal: Long = -1): Long {
        return sharedPreference.getLong(key, defVal)
    }

    @JvmOverloads
    fun getIntFromPreference(key: String, defVal: Int = -1): Int {
        return sharedPreference.getInt(key, defVal)
    }
}