/*
 * Copyright (c) 2018. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.kevalpatel2106.yip.core.di

import android.app.Application
import android.content.Context
import com.kevalpatel2106.yip.repo.db.YipDatabase
import com.kevalpatel2106.yip.repo.di.RepositoryModule
import com.kevalpatel2106.yip.repo.utils.NtpProvider
import com.kevalpatel2106.yip.repo.utils.SharedPrefsProvider
import dagger.Component
import javax.inject.Singleton

/**
 * Created by Kevalpatel2106 on 17-Apr-18.
 *
 * @author <a href="https://github.com/kevalpatel2106">kevalpatel2106</a>
 */
@Singleton
@Component(modules = [CoreModule::class, RepositoryModule::class])
interface CoreComponent {

    fun getContext(): Context

    fun getApplication(): Application

    fun getDatabase(): YipDatabase

    fun getNtp(): NtpProvider

    fun getSharedPrefs(): SharedPrefsProvider

    companion object {
        fun build(application: Application): CoreComponent {
            return DaggerCoreComponent.builder()
                    .coreModule(CoreModule(application))
                    .repositoryModule(RepositoryModule(application))
                    .build()
        }
    }
}