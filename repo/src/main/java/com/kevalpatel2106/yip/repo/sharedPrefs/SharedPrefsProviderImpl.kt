/*
 * Copyright (c) 2018. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.kevalpatel2106.yip.repo.sharedPrefs

import android.content.SharedPreferences
import androidx.core.content.edit
import com.f2prateek.rx.preferences2.RxSharedPreferences
import io.reactivex.Observable

/**
 * Created by Keval on 20-Aug-16.
 * Class contains all the helper functions to deal with shared prefs.
 *
 * @author <a href="https://github.com/kevalpatel2106">kevalpatel2106</a>
 */
internal class SharedPrefsProviderImpl(
    private val sharedPreference: SharedPreferences
) : SharedPrefsProvider {
    private val rxPreferences = RxSharedPreferences.create(sharedPreference)

    /**
     * Remove and clear data from preferences for given field
     *
     * @param key key of preference field to remove
     */
    override fun removePreferences(key: String) {
        sharedPreference.edit { remove(key) }
    }

    /**
     * Save colorInt to shared preference
     *
     * @param key key of preference field
     *
     * @param value colorInt to store
     */
    override fun savePreferences(key: String, value: String?) {
        sharedPreference.edit { putString(key, value) }
    }

    /**
     * Save colorInt to shared preference
     *
     * @param key key of preference field
     *
     * @param value colorInt to store
     */
    override fun savePreferences(key: String, value: Boolean) {
        sharedPreference.edit { putBoolean(key, value) }
    }

    /**
     * Save colorInt to shared preference
     *
     * @param key key of preference field
     *
     * @param value colorInt to store in int
     */
    override fun savePreferences(key: String, value: Int) {
        sharedPreference.edit { putInt(key, value) }
    }

    /**
     * Save colorInt to shared preference
     *
     * @param key key of preference field
     *
     * @param value colorInt to store in long
     */
    override fun savePreferences(key: String, value: Long) {
        sharedPreference.edit { putLong(key, value) }
    }

    override fun observeStringFromPreference(key: String, defVal: String?): Observable<String> {
        return run {
            if (defVal != null) {
                rxPreferences.getString(key, defVal)
            } else {
                rxPreferences.getString(key)
            }
        }.asObservable()
    }

    override fun getStringFromPreference(key: String, defVal: String?): String? =
        sharedPreference.getString(key, defVal)

    override fun getBoolFromPreference(key: String, defVal: Boolean): Boolean =
        sharedPreference.getBoolean(key, defVal)

    override fun getLongFromPreference(key: String, defVal: Long): Long =
        sharedPreference.getLong(key, defVal)

    override fun getIntFromPreference(key: String, defVal: Int): Int =
        sharedPreference.getInt(key, defVal)
}
